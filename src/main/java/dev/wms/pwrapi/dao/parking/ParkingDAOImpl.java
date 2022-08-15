package dev.wms.pwrapi.dao.parking;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Repository;

import dev.wms.pwrapi.dto.parking.deserialization.ParkingArrayElement;
import dev.wms.pwrapi.dto.parking.deserialization.ParkingResponse;
import dev.wms.pwrapi.dto.parking.Parking;
import dev.wms.pwrapi.utils.parking.ParkingGeneralUtils;
import dev.wms.pwrapi.utils.parking.exceptions.WrongResponseCode;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Repository
public class ParkingDAOImpl implements ParkingDAO {


    @Override
    public ArrayList<Parking> getProcessedParkingInfo() throws IOException{

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

    //  System.out.println(response.body().string());

    ParkingResponse deserialized = new ObjectMapper().readValue(response.body().string(), ParkingResponse.class);
    System.out.println(deserialized);

    //process the response
    
    if(deserialized.getSuccess() != 0) throw new WrongResponseCode();

    for(ParkingArrayElement parking : deserialized.getPlaces()){

        Parking toAdd = new Parking().builder()
            .name(ParkingGeneralUtils.determineParking(parking.getParking_id()))
            .lastUpdate(parking.getCzas_pomiaru())
            .leftPlaces(Integer.valueOf(parking.getLiczba_miejsc()))
            .trend(Integer.valueOf(parking.getTrend()))
            .build();

        result.add(toAdd);
    }


        return result;
    }

    @Override
    public String getRawParkingData() throws IOException{

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


    return response.body().string();

    }

    
}
