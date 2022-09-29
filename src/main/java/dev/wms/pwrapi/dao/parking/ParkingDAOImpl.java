package dev.wms.pwrapi.dao.parking;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.wms.pwrapi.dto.parking.ParkingWithHistory;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Repository;

import dev.wms.pwrapi.dto.parking.Parking;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Repository
public class ParkingDAOImpl implements ParkingDAO {

    @Override
    public List<Parking> getProcessedParkingInfo() throws IOException{
        return parseProcessed(fetchParkingWebsite());
    }

    @Override
    public List<ParkingWithHistory> getRawParkingData() throws IOException{
        return parseWithDetails(fetchParkingWebsite());
    }

    private Document fetchParkingWebsite() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://skd.pwr.edu.pl/")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"105\", \"Not)A;Brand\";v=\"8\", \"Chromium\";v=\"105\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("host", "skd.pwr.edu.pl")
                .build();
        Response response = client.newCall(request).execute();
        return Jsoup.parse(response.body().string());
    }

    private List<Parking> parseProcessed(Element page){
        List<Parking> result = new ArrayList<>();
         Matcher matcher = Pattern.compile("\"type\":\"put\",\"key\":\"text\",\"feat\":7,\"value\":\"\\d+").matcher(page.html());
         matcher.find();

        result.add(new Parking("Parking Wrońskiego", getMeasurmentTime(), getPlacesFromResponse(matcher), 0));
        matcher.find();
        result.add(new Parking("C13", getMeasurmentTime(), getPlacesFromResponse(matcher), 0));
        matcher.find();
        result.add(new Parking("D20", getMeasurmentTime(), getPlacesFromResponse(matcher), 0));
        matcher.find();
        result.add(new Parking("Geocentrum", getMeasurmentTime(), getPlacesFromResponse(matcher), 0));
        matcher.find();
        result.add(new Parking("Architektura", getMeasurmentTime(), getPlacesFromResponse(matcher), 0));

        return result;
    }

    private List<ParkingWithHistory> parseWithDetails(Element page){
        List<ParkingWithHistory> result = new ArrayList<>();
        Matcher matcher = Pattern.compile("(?<=\\\\\"data\\\\\":\\[)(.*?)(?=\\])").matcher(page.html());
        matcher.find();
        result.add(new ParkingWithHistory("Parking Wrońskiego", getMeasurmentTime(), matcher.group()));
        matcher.find();
        result.add(new ParkingWithHistory("C13", getMeasurmentTime(), matcher.group()));
        matcher.find();
        result.add(new ParkingWithHistory("D20", getMeasurmentTime(), matcher.group()));
        matcher.find();
        result.add(new ParkingWithHistory("Geocentrum", getMeasurmentTime(), matcher.group()));
        matcher.find();
        result.add(new ParkingWithHistory("Architektura", getMeasurmentTime(), matcher.group()));

        return result;
    }

    @NotNull
    private String getMeasurmentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private int getPlacesFromResponse(Matcher matcher) {
        return Integer.parseInt(matcher.group().split("value\":\"")[1]);
    }

}
