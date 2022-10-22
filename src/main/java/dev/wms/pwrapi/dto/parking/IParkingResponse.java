package dev.wms.pwrapi.dto.parking;

import lombok.Data;

@Data
public class IParkingResponse {
    private int success;
    private DataWithLabels slots;
}
