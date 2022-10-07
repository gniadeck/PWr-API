package dev.wms.pwrapi.dao.parking;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.wms.pwrapi.dto.parking.ParkingWithHistory;
import dev.wms.pwrapi.dto.parking.deserialization.ParkingWithHistoryArrayElement;
import dev.wms.pwrapi.dto.parking.deserialization.ParkingWithHistoryResponse;
import dev.wms.pwrapi.utils.parking.ParkingGeneralUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import dev.wms.pwrapi.dto.parking.deserialization.ParkingArrayElement;
import dev.wms.pwrapi.dto.parking.deserialization.ParkingResponse;
import dev.wms.pwrapi.dto.parking.Parking;
import dev.wms.pwrapi.utils.parking.exceptions.WrongResponseCode;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Repository
@Primary
public class IParkingDAO implements ParkingDAO {


    @Override
    public ArrayList<Parking> getProcessedParkingInfo() throws IOException {

        ArrayList<Parking> result = new ArrayList<Parking>();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, "o=get_parks&ts=1652019293233");
        Request request = new Request.Builder()
                .url("https://iparking.pwr.edu.pl/modules/iparking/scripts/ipk_operations.php")
                .method("POST", body)
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"100\", \"Google Chrome\";v=\"100\"")
                .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("Referer", "https://iparking.pwr.edu.pl/")
                .addHeader("Origin", "https://iparking.pwr.edu.pl")
                .build();
        Response response = client.newCall(request).execute();

        ParkingResponse deserializedResponse = new ObjectMapper().readValue(response.body().string(), ParkingResponse.class);

        if (deserializedResponse.getSuccess() != 0) throw new WrongResponseCode();

        DateTimeFormatter parkingFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (ParkingArrayElement parking : deserializedResponse.getPlaces()) {

            Parking toAdd = new Parking().builder()
                    .name(ParkingGeneralUtils.determineParking(parking.getParking_id()))
                    .lastUpdate(LocalDateTime.parse(parking.getCzas_pomiaru(), parkingFormatter).toString())
                    .leftPlaces(Integer.valueOf(parking.getLiczba_miejsc()))
                    .trend(Integer.valueOf(parking.getTrend()))
                    .build();

            result.add(toAdd);
        }


        return result;
    }

    @Override
    public List<ParkingWithHistory> getRawParkingData() throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, "o=get_parks&ts=1652019293233");
        Request request = new Request.Builder()
                .url("https://iparking.pwr.edu.pl/modules/iparking/scripts/ipk_operations.php")
                .method("POST", body)
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"100\", \"Google Chrome\";v=\"100\"")
                .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("Referer", "https://iparking.pwr.edu.pl/")
                .addHeader("Origin", "https://iparking.pwr.edu.pl")
                //    .addHeader("Cookie", "PHPSESSID=sgn0fqbs1vg9bjotuum1aha957")
                .build();
        Response response = client.newCall(request).execute();
        String stringResponse = response.body().string();
        System.out.println(stringResponse);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        ParkingWithHistoryResponse parkingWithHistoryResponses = mapper.readValue(stringResponse, ParkingWithHistoryResponse.class);
        List<ParkingWithHistory> result = new ArrayList<>();
        for(ParkingWithHistoryArrayElement parking : parkingWithHistoryResponses.getPlaces()){

            ParkingWithHistory toAdd = ParkingWithHistory.builder()
                    .name(ParkingGeneralUtils.determineParking(parking.getParking_id()))
                    .lastUpdate(parking.getCzas_pomiaru())
                    .history(parseHistory(parking.getChart().getX(), parking.getChart().getData()).toString().replace("{","").replace("}", ""))
                    .build();
            result.add(toAdd);
        }
        return result;
    }

    private Map<String, String> parseHistory(List<String> hours, List<String> state){
        Map<String, String> states = new TreeMap<>();
        for(int i = 0; i < hours.size(); i++){
            states.put(hours.get(i), state.get(i));
        }
        return states;
    }


}