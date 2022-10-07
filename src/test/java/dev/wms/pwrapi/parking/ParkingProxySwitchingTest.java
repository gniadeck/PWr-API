package dev.wms.pwrapi.parking;

import dev.wms.pwrapi.dao.parking.ParkingDAO;
import dev.wms.pwrapi.service.parking.ParkingProxy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ParkingProxySwitchingTest {

    @Autowired
    private ParkingProxy parkingProxy;
    @MockBean(name = "SKDParkingDAO")
    private ParkingDAO SKDParkingDAO;
    @MockBean(name = "IParkingDAO")
    private ParkingDAO IParkingDAO;

    @Test
    public void parkingProxyShouldCallSkdServiceIfIparkingIsUnavailable() throws IOException {
        when(IParkingDAO.getProcessedParkingInfo())
                .thenThrow(new RuntimeException("Cannot access university system!"));
        when(SKDParkingDAO.getProcessedParkingInfo())
                .thenReturn(Collections.emptyList());

        parkingProxy.getParkingState();

        verify(SKDParkingDAO, times(1))
                .getProcessedParkingInfo();
    }

    @Test
    public void parkingProxyShouldCallSkdServiceIfIparkingIsUnavailableForRawData() throws IOException {
        when(IParkingDAO.getRawParkingData())
                .thenThrow(new RuntimeException("Cannot access university system!"));
        when(SKDParkingDAO.getRawParkingData())
                .thenReturn(Collections.emptyList());

        parkingProxy.getParkingWithHistory();

        verify(SKDParkingDAO, times(1))
                .getRawParkingData();
    }


    @Test
    public void parkingProxyShouldCallIparkingServiceIfIparkingIsAvailable() throws IOException {
        when(IParkingDAO.getProcessedParkingInfo())
                .thenReturn(Collections.emptyList());
        when(SKDParkingDAO.getProcessedParkingInfo())
                .thenThrow(new RuntimeException("I shouldn't have been thrown..."));

        parkingProxy.getParkingState();

        verify(IParkingDAO, times(1))
                .getProcessedParkingInfo();

        verifyNoInteractions(SKDParkingDAO);
    }

    @Test
    public void parkingProxyShouldCallIparkingServiceIfIparkingIsAvailableForHistoryEndpoint() throws IOException {
        when(IParkingDAO.getRawParkingData())
                .thenReturn(Collections.emptyList());
        when(SKDParkingDAO.getRawParkingData())
                .thenThrow(new RuntimeException("I shouldn't have been thrown..."));

        parkingProxy.getParkingWithHistory();

        verify(IParkingDAO, times(1))
                .getRawParkingData();

        verifyNoInteractions(SKDParkingDAO);
    }


    @Test
    public void parkingProxyShouldThrowOnBothServicesunavailable() throws IOException {
        when(IParkingDAO.getProcessedParkingInfo())
                .thenThrow(new RuntimeException("I'm not working"));
        when(SKDParkingDAO.getProcessedParkingInfo())
                .thenThrow(new RuntimeException("I'm not working"));

        assertThrows(Throwable.class, () -> parkingProxy.getParkingState());

        verify(IParkingDAO, times(1))
                .getProcessedParkingInfo();

        verify(SKDParkingDAO, times(1))
                .getProcessedParkingInfo();
    }

    @Test
    public void parkingProxyShouldThrowOnBothServicesunavailableForHistoryEndpoint() throws IOException {
        when(IParkingDAO.getRawParkingData())
                .thenThrow(new RuntimeException("I'm not working"));
        when(SKDParkingDAO.getRawParkingData())
                .thenThrow(new RuntimeException("I'm not working"));

        assertThrows(Throwable.class, () -> parkingProxy.getParkingWithHistory());

        verify(IParkingDAO, times(1))
                .getRawParkingData();

        verify(SKDParkingDAO, times(1))
                .getRawParkingData();
    }


}
