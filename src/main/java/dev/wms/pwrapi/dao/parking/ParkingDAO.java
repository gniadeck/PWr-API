package dev.wms.pwrapi.dao.parking;

import dev.wms.pwrapi.dto.parking.Parking;
import dev.wms.pwrapi.dto.parking.ParkingWithHistory;

import java.io.IOException;
import java.util.List;

public interface ParkingDAO {
    /**
     * Returns processed parking data. Server response is mapped to Parking objects.
     * @return List of parking objects
     * @throws IOException When deserialization goes wrong
     */
    List<Parking> getProcessedParkingInfo() throws IOException;

    /**
     * Returns unprocessed response from server. Works like proxy
     * @return JSON Response from server
     * @throws IOException When deserialization goes wrong
     */
    List<ParkingWithHistory> getRawParkingData() throws IOException;
}
