package dev.wms.pwrapi.dao.news;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dev.wms.pwrapi.dto.news.*;
import dev.wms.pwrapi.utils.http.HttpUtils;
import okhttp3.OkHttpClient;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class GeneralNewsDAO {

    private final DateTimeFormatter rssFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
    private final DateTimeFormatter goalFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final Pattern datePattern = Pattern.compile("\\d{2} [a-zA-Z]{3} \\d{4}");

    public Channel parsePwrRSS(String rssUrl) {
        OkHttpClient client = new OkHttpClient();
        String response = HttpUtils.makeRequestWithClientAndGetString(client, rssUrl);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        xmlMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            Rss items = xmlMapper.readValue(response, Rss.class);
            for(Item item : items.getChannel().getItem()) reformatDate(item);

            return items.getChannel();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void reformatDate(Item item){
        Matcher matcher = datePattern.matcher(item.getPubDate());
        matcher.find();
        LocalDate parsedDate = LocalDate.parse(matcher.group(), rssFormatter);
        item.setPubDate(parsedDate.format(goalFormatter));
    }

    public Channel getFacultyNews(FacultyType faculty) {
        if(faculty.isRss){
            return parsePwrRSS(faculty.url);
        } else {
            return parsePwrHTML(faculty.url);
        }
    }

    private Channel parsePwrHTML(String url) {
        OkHttpClient client = new OkHttpClient();
        Document document = HttpUtils.makeRequestWithClientAndGetDocument(client, url);

        Elements newsBoxes = document.getElementsByClass("news-box");
        newsBoxes.removeIf(box -> box.text().isEmpty());

        List<Item> channelItems = newsBoxes.parallelStream()
                .map(newsBox -> parseItem(newsBox, url))
                .toList();


        return Channel.builder()
                .title(document.getElementsByClass("portal-title").first().text())
                .link(url)
                .description("")
                .item(channelItems)
                .build();
    }

    private Item parseItem(Element element, String url){
        Element textDiv = element.getElementsByClass("col-text").first();
        return Item.builder()
                .title(textDiv.getElementsByClass("title").first().attr("title"))
                .link(getDomainFromNewsUrl(url) + textDiv.getElementsByClass("title").first().attr("href"))
                .pubDate(textDiv.getElementsByClass("date").text().replace("Data: ","").strip()
                        .split("Kategoria:")[0].strip())
                .description(textDiv.getElementsByTag("p").get(1).text().replace("... więcej","")
                        .replace("więcej","").strip())
                .build();
    }

    private String getDomainFromNewsUrl(String url){
        return url.split(".pl")[0] + ".pl";
    }
}
