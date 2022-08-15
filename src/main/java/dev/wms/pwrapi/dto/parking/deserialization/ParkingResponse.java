package dev.wms.pwrapi.dto.parking.deserialization;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ParkingResponse {

    @JsonProperty("success")
    private int success;
    @JsonProperty("places")
    private List<ParkingArrayElement> places;
    
}
