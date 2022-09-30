package dev.wms.pwrapi.dto.parking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ParkingWithHistory {
    private String name;
    private String lastUpdate;
    private String history;

}
