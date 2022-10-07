package dev.wms.pwrapi.dto.parking.deserialization;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ParkingWithHistoryResponse {
    private int success;
    private List<ParkingWithHistoryArrayElement> places;
}
