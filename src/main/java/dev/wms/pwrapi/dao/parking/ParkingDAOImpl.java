package dev.wms.pwrapi.dao.parking;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.wms.pwrapi.dto.parking.ParkingWithHistory;
import dev.wms.pwrapi.utils.http.HttpUtils;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Repository;

import dev.wms.pwrapi.dto.parking.Parking;
import okhttp3.OkHttpClient;

@Repository
public class ParkingDAOImpl implements ParkingDAO {

    public static final String PARKING_WRONSKIEGO = "Parking Wrońskiego";
    public static final String C_13 = "C13";
    public static final String D_20 = "D20";
    public static final String GEOCENTRUM = "Geocentrum";
    public static final String ARCHITEKTURA = "Architektura";

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
        return HttpUtils.makeRequestWithClientAndGetDocument(client, "https://skd.pwr.edu.pl/");
    }

    private List<Parking> parseProcessed(Element page){
        List<Parking> result = new ArrayList<>();
         Matcher matcher = Pattern.compile("\"type\":\"put\",\"key\":\"text\",\"feat\":7,\"value\":\"\\d+").matcher(page.html());
         matcher.find();
        result.add(new Parking(PARKING_WRONSKIEGO, getMeasurmentTime(), getPlacesFromResponse(matcher), 0));
        matcher.find();
        result.add(new Parking(C_13, getMeasurmentTime(), getPlacesFromResponse(matcher), 0));
        matcher.find();
        result.add(new Parking(D_20, getMeasurmentTime(), getPlacesFromResponse(matcher), 0));
        matcher.find();
        result.add(new Parking(GEOCENTRUM, getMeasurmentTime(), getPlacesFromResponse(matcher), 0));
        matcher.find();
        result.add(new Parking(ARCHITEKTURA, getMeasurmentTime(), getPlacesFromResponse(matcher), 0));

        return result;
    }

    private List<ParkingWithHistory> parseWithDetails(Element page){
        List<ParkingWithHistory> result = new ArrayList<>();
        Matcher matcher = Pattern.compile("(?<=\\\\\"data\\\\\":\\[)(.*?)(?=\\])").matcher(page.html());
        matcher.find();
        result.add(new ParkingWithHistory(PARKING_WRONSKIEGO, getMeasurmentTime(), sanitizeArray(matcher.group())));
        matcher.find();
        result.add(new ParkingWithHistory(C_13, getMeasurmentTime(), sanitizeArray(matcher.group())));
        matcher.find();
        result.add(new ParkingWithHistory(D_20, getMeasurmentTime(), sanitizeArray(matcher.group())));
        matcher.find();
        result.add(new ParkingWithHistory(GEOCENTRUM, getMeasurmentTime(), sanitizeArray(matcher.group())));
        matcher.find();
        result.add(new ParkingWithHistory(ARCHITEKTURA, getMeasurmentTime(), sanitizeArray(matcher.group())));

        return result;
    }

    private String sanitizeArray(String array){
        return "[" + array + "]";
    }

    @NotNull
    private String getMeasurmentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private int getPlacesFromResponse(Matcher matcher) {
        return Integer.parseInt(matcher.group().split("value\":\"")[1]);
    }

}
