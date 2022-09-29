package dev.wms.pwrapi.parking;

import dev.wms.pwrapi.dao.parking.ParkingDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ParkingDAOTest {

    @Autowired
    private ParkingDAO parkingDAO;

    @Test
    public void getProcessedParkingInfoShouldWork() throws IOException {
        System.out.println(parkingDAO.getRawParkingData());
    }

}
