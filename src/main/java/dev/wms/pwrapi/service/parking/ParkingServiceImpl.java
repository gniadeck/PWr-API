package dev.wms.pwrapi.service.parking;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import dev.wms.pwrapi.dto.parking.Parking;
import dev.wms.pwrapi.dto.parking.ParkingWithHistory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.wms.pwrapi.dao.parking.ParkingDAO;

@Service
@AllArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private ParkingProxy parkingProxy;

    @Override
    public List<Parking> getParkingData() throws IOException{
        return parkingProxy.getParkingState();
    }

    @Override
    public List<ParkingWithHistory> getRawParkingData() {
        return parkingProxy.getParkingWithHistory();
    }

}
