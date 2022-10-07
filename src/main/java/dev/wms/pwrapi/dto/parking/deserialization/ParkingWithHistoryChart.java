package dev.wms.pwrapi.dto.parking.deserialization;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ParkingWithHistoryChart {
    @JsonProperty("x")
    private List<String> x;
    @JsonProperty("data")
    private List<String> data;
}
