package dev.wms.pwrapi.service.parking;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import dev.wms.pwrapi.dto.parking.Parking;
import dev.wms.pwrapi.dto.parking.ParkingWithHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.wms.pwrapi.dao.parking.ParkingDAO;

@Service
public class ParkingServiceImpl implements ParkingService {

    private ParkingDAO parkingDAO;

    @Autowired
    public ParkingServiceImpl(ParkingDAO parkingDAO){
        this.parkingDAO = parkingDAO;
    }

    @Override
    public List<Parking> getParkingData() throws JsonProcessingException, IOException{
        return parkingDAO.getProcessedParkingInfo();
    }

    @Override
    public List<ParkingWithHistory> getRawParkingData() throws IOException{
        return parkingDAO.getRawParkingData();
    }

}
