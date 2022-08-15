package dev.wms.pwrapi.dto.parking.deserialization;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Data;
import lombok.ToString;


@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParkingArrayElement {

    @JsonProperty("id")
    private String id;
    @JsonProperty("parking_id")
    private String parking_id;
    @JsonProperty("czas_pomiaru")
    private String czas_pomiaru;
    @JsonProperty("liczba_miejsc")
    private String liczba_miejsc;
    @JsonProperty("trend")
    private String trend; 
    
}
