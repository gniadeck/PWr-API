package dev.wms.pwrapi.dao.parking;

import dev.wms.pwrapi.dto.parking.Parking;

import java.io.IOException;
import java.util.ArrayList;

public interface ParkingDAO {
    /**
     * Returns processed parking data. Server response is mapped to Parking objects.
     * @return List of parking objects
     * @throws IOException When deserialization goes wrong
     */
    ArrayList<Parking> getProcessedParkingInfo() throws IOException;

    /**
     * Returns unprocessed response from server. Works like proxy
     * @return JSON Response from server
     * @throws IOException When deserialization goes wrong
     */
    String getRawParkingData() throws IOException;
}
