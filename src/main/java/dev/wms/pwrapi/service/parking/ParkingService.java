package dev.wms.pwrapi.service.parking;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.dto.parking.Parking;

import java.io.IOException;
import java.util.List;

public interface ParkingService {

    List<Parking> getParkingData() throws JsonProcessingException, IOException;

    String getRawParkingData() throws IOException;
}
