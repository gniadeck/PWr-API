package dev.wms.pwrapi.dao.parking;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.wms.pwrapi.dto.parking.DataWithLabels;
import dev.wms.pwrapi.dto.parking.ParkingWithHistory;
import dev.wms.pwrapi.dto.parking.deserialization.ParkingWithHistoryArrayElement;
import dev.wms.pwrapi.dto.parking.deserialization.ParkingWithHistoryResponse;
import dev.wms.pwrapi.utils.http.HttpUtils;
import dev.wms.pwrapi.utils.parking.ParkingDateUtils;
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
public class IParkingDAO implements ParkingDAO {


    @Override
    public ArrayList<Parking> getProcessedParkingInfo() throws IOException {

        ArrayList<Parking> result = new ArrayList<>();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"o\":\"get_parks\",\"ts\":\"1665147767564\"}");
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

        ParkingResponse deserializedResponse = new ObjectMapper().readValue(
                HttpUtils.makeRequestWithClientAndGetString(client, request), ParkingResponse.class);

        if (deserializedResponse.getSuccess() != 0) throw new WrongResponseCode();


        LocalDateTime now = ParkingDateUtils.getDateTimeInPoland();
        for (ParkingArrayElement parking : deserializedResponse.getPlaces()) {

            Parking toAdd = new Parking().builder()
                    .name(ParkingGeneralUtils.determineParking(parking.getParking_id()))
                    .lastUpdate(now.toString())
                    .leftPlaces(Integer.parseInt(parking.getLiczba_miejsc()))
                    .trend(Integer.parseInt(parking.getTrend()))
                    .build();

            result.add(toAdd);
        }

        return result;
    }

    @Override
    public List<ParkingWithHistory> getRawParkingData() throws IOException {
        Set<Integer> parkingIds = ParkingGeneralUtils.getParkingIds();
        List<ParkingWithHistoryResponse> responses = new ArrayList<>();
        parkingIds.forEach(p -> responses.add(requestDataForParkingId(p)));

        LocalDateTime now = ParkingDateUtils.getDateTimeInPoland();
        List<ParkingWithHistory> result = new ArrayList<>();

        for(ParkingWithHistoryResponse parking : responses){

            ParkingWithHistory toAdd = ParkingWithHistory.builder()
                    .name(ParkingGeneralUtils.determineParking(parking.getParkingId().toString()))
                    .lastUpdate(now.toString())
                    .history(parseHistory(parking.getSlots()).toString().replace("{","").replace("}", ""))
                    .build();
            result.add(toAdd);

        }
        return result;
    }


    private ParkingWithHistoryResponse requestDataForParkingId(int parkingId) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"o\":\"get_today_chart\",\"i\":\"" + parkingId + "\"}");
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

        String stringResponse = HttpUtils.makeRequestWithClientAndGetString(client, request);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        try {
            ParkingWithHistoryResponse result = mapper.readValue(stringResponse, ParkingWithHistoryResponse.class);
            result.setParkingId(parkingId);
            return result;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private Map<String, String> parseHistory(DataWithLabels dataWithLabels){
        Map<String, String> states = new TreeMap<>();
        for(int i = 0; i < dataWithLabels.getLabels().size(); i++){
            states.put(dataWithLabels.getLabels().get(i), dataWithLabels.getData().get(i));
        }
        return states;
    }


}