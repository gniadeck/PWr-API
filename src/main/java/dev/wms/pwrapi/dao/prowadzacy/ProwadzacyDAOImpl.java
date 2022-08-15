package dev.wms.pwrapi.dao.prowadzacy;

import dev.wms.pwrapi.entity.prowadzacy.ProwadzacyDay;
import dev.wms.pwrapi.entity.prowadzacy.ProwadzacyLesson;
import dev.wms.pwrapi.entity.prowadzacy.ProwadzacyResult;
import dev.wms.pwrapi.utils.prowadzacy.exceptions.EmptyResultsException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class ProwadzacyDAOImpl implements ProwadzacyDAO {

    private final String websiteURL = "https://www.prowadzacy.wit.pwr.edu.pl/";

    @Override
    public String connectToWebsite(){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(websiteURL)
                .build();

        Response response;
        try {
           response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(response.code() == 200){
            return "OK " + LocalDateTime.now();
        } else {
            return "Wrong response code: " + response.code();
        }

    }

    @Override
    public ProwadzacyResult getPlanForTeacherQuery(String query, Integer offset){
        return getPlanFromURL(websiteURL + "wyniki.php?pole%5B%5D=" + query.toLowerCase(), offset);
    }

    @Override
    public ProwadzacyResult getPlanForRoomQuery(String building, String room, Integer offset){
        return getPlanFromURL(websiteURL + "wyniki.php?pole%5B%5D="
                + building + "%3A+" + room + "%2C+", offset);
    }

    @Override
    public ProwadzacyResult getPlanForSubjectQuery(String query, Integer offset){
        return getPlanFromURL(websiteURL + "wyniki.php?pole%5B%5D=" + query +"%2C+", offset);
    }

    private ProwadzacyResult getPlanFromURL(String url, Integer offset){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Document page;
        try(Response response = client.newCall(request).execute()) {

            page = Jsoup.parse(response.body().string());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //process offset
        if(offset != null && offset != 0){
            DateTimeFormatter requestFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate mondayDate = LocalDate.parse(page.getElementsByAttributeValue("id","days")
                            .first().text().split(" ")[1],
                    requestFormatter);

            mondayDate = mondayDate.plusWeeks(offset);

            Request offsetRequest = new Request.Builder()
                    .url(url + "&rez=" + requestFormatter.format(mondayDate))
                    .build();

            try(Response response = client.newCall(offsetRequest).execute()) {

                page = Jsoup.parse(response.body().string());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if(page.getElementById("wyniki").text().contains("Podana fraza nie została odnaleziona")){
            throw new EmptyResultsException();
        }

        List<Element> days = page.getElementsByClass("day");
        List<Element> daysHeaders = page.getElementsByAttributeValue("id","days").first().getElementsByTag("div");
        daysHeaders.remove(0);

        List<ProwadzacyDay> processedDays = new ArrayList<>(Collections.nCopies(8, new ProwadzacyDay()));


        int dayIndex = 0;

        Optional<Element> titleOptional = Optional.ofNullable(page.getElementsByTag("left").first());
        String title = null;
        if(titleOptional.isPresent()){
            title = titleOptional.get().text();
            if(title.equals("")){
                throw new EmptyResultsException();
            }
        }


        for(Element day : days){

            List<Element> subjects = day.getElementsByClass("entry");
            List<ProwadzacyLesson> dayLessons = new ArrayList<>();

            for(Element lessons : subjects){

                String pClassText = lessons.getElementsByClass("p").first().text();
                String teacherName = pClassText.equals("") ? title : pClassText;

                ProwadzacyLesson lesson = ProwadzacyLesson.builder()
                        .time(lessons.getElementsByClass("h").text())
                        .title(lessons.getElementsByClass("n").text())
                        .location(lessons.getElementsByClass("l").text())
                        .teacher(teacherName)
                        .build();

                dayLessons.add(lesson);

            }

            dayLessons.sort(Comparator.comparing(ProwadzacyLesson::getTime));

            ProwadzacyDay processedDay = ProwadzacyDay.builder()
                    .date(daysHeaders.get(dayIndex).text())
                    .lessons(dayLessons)
                    .build();

            processedDays.set(dayIndex, processedDay);

            dayIndex++;
        }

        Optional<Element> icalElement = page.getElementsByTag("a").stream()
                .filter(element -> element.text().contains("ical"))
                .findFirst();

        String icalLink = null;
        if(icalElement.isPresent()){
            icalLink = websiteURL + icalElement.get().attr("href");
        }

        if(title == null){
            title = processedDays.get(0).getLessons().get(0).getTitle();
        }

        int index = 0;
        ProwadzacyResult result = ProwadzacyResult.builder()
                .title(title)
                .pn(processedDays.get(index++))
                .wt(processedDays.get(index++))
                .sr(processedDays.get(index++))
                .czw(processedDays.get(index++))
                .pt(processedDays.get(index++))
                .sb(processedDays.get(index++))
                .nd(processedDays.get(index++))
                .icalLink(icalLink)
                .build();

        return result;

    }

}
