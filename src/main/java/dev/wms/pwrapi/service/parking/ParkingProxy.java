package dev.wms.pwrapi.service.parking;

import dev.wms.pwrapi.dao.parking.ParkingDAO;
import dev.wms.pwrapi.dto.parking.Parking;
import dev.wms.pwrapi.dto.parking.ParkingWithHistory;
import dev.wms.pwrapi.utils.parking.ParkingDateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * In order to reduce stress on university servers, we use proxy design pattern so
 * server is called if we requested data minimum one minute before
 */
@AllArgsConstructor
@Component
public class ParkingProxy {
    private ParkingDAO parkingDAO;
    private List<Parking> parkingState;
    private List<ParkingWithHistory> parkingWithHistoryState;

    public List<Parking> getParkingState() throws IOException {
        if(parkingStateQualifies(parkingState)){
            parkingState = parkingDAO.getProcessedParkingInfo();
        }
        return parkingState;
    }

    public List<ParkingWithHistory> getParkingWithHistory() throws IOException {
        if(parkingStateWithHistoryQualifiesForUpdate(parkingWithHistoryState)){
            parkingWithHistoryState = parkingDAO.getRawParkingData();

        }
        return parkingWithHistoryState;
    }

    private boolean parkingStateQualifies(List<Parking> parkingState){
       return parkingState == null || parkingState.isEmpty() || parseUpdateTime(parkingState.get(0).getLastUpdate())
               .isBefore(ParkingDateUtils.getDateTimeInPoland().minusMinutes(1));
    }

    private boolean parkingStateWithHistoryQualifiesForUpdate(List<ParkingWithHistory> parkingWithHistoryState){
        return parkingState == null || parkingWithHistoryState.isEmpty() || parseUpdateTime(parkingWithHistoryState.get(0).getLastUpdate()).isBefore(
                ParkingDateUtils.getDateTimeInPoland().minusMinutes(1)
        );
    }

    private LocalDateTime parseUpdateTime(String lastUpdate){
        return LocalDateTime.parse(lastUpdate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
