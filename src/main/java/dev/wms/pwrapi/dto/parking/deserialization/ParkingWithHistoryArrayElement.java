package dev.wms.pwrapi.dto.parking.deserialization;

import lombok.Data;

@Data
public
class ParkingWithHistoryArrayElement {
    private String id;
    private String parking_id;
    private String czas_pomiaru;
    private String liczba_miejsc;
    private String trend;
    private ParkingWithHistoryChart chart;
}
