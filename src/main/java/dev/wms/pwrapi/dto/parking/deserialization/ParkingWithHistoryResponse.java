package dev.wms.pwrapi.dto.parking.deserialization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.wms.pwrapi.dto.parking.DataWithLabels;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ParkingWithHistoryResponse {
    private int success;
    private DataWithLabels slots;
    @JsonIgnore
    private Integer parkingId;
}
