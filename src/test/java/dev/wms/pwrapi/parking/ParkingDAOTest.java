package dev.wms.pwrapi.parking;

import dev.wms.pwrapi.dao.parking.ParkingDAO;
import dev.wms.pwrapi.dto.parking.Parking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ParkingDAOTest {

    @Autowired
    private ParkingDAO parkingDAO;

    /**
     * Because of migration to SKD service, trend feature is no longer supported, and trend should be equal
     * to zero
     * @throws IOException
     */
    @Test
    public void trendShouldAlwaysBeZero() throws IOException {
        List<Parking> result = parkingDAO.getProcessedParkingInfo();
        for(Parking parking : result){
            assertEquals(0, parking.getTrend());
        }
    }

}
