package dev.wms.pwrapi.parking;

import dev.wms.pwrapi.dto.parking.Parking;
import dev.wms.pwrapi.dto.parking.ParkingWithHistory;
import dev.wms.pwrapi.service.parking.ParkingProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ParkingProxyTest {

    @Autowired
    private ParkingProxy parkingProxy;

    @Test
    public void parkingProxyShouldntUpdateDataForOneMinute() throws IOException {
        List<Parking> result = parkingProxy.getParkingState();
        for(int i = 0; i < 10; i++){
            assertEquals(result.get(0).getLastUpdate(), parkingProxy.getParkingState().get(0).getLastUpdate());
        }
        List<ParkingWithHistory> resultWithHistory = parkingProxy.getParkingWithHistory();
        for(int i = 0; i < 10; i++){
            assertEquals(resultWithHistory.get(0).getLastUpdate(), parkingProxy.getParkingWithHistory().get(0).getLastUpdate());
        }

    }




}
