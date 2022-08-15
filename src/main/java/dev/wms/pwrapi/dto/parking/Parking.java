package dev.wms.pwrapi.dto.parking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parking {


    private String name;
    private String lastUpdate;
    private int leftPlaces;
    private int trend;
    
}
