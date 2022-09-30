package dev.wms.pwrapi.service.parking;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.dto.parking.Parking;
import dev.wms.pwrapi.dto.parking.ParkingWithHistory;

import java.io.IOException;
import java.util.List;

public interface ParkingService {

    List<Parking> getParkingData() throws JsonProcessingException, IOException;

    List<ParkingWithHistory> getRawParkingData() throws IOException;
}
