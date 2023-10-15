package dev.wms.pwrapi.service.parking;

import dev.wms.pwrapi.dao.parking.ParkingDAO;
import dev.wms.pwrapi.dto.parking.Parking;
import dev.wms.pwrapi.dto.parking.ParkingWithHistory;
import dev.wms.pwrapi.utils.parking.ParkingDateUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * In order to reduce stress on university servers, we use proxy design pattern so
 * server is called if we requested data minimum one minute before
 */
@Component
public class ParkingProxy {

    private final ParkingDAO iParkingDAO;
    private final ParkingDAO skdParkingDAO;
    private List<Parking> parkingState;
    private List<ParkingWithHistory> parkingWithHistoryState;

    public ParkingProxy(@Qualifier("IParkingDAO") ParkingDAO iParkingDAO, @Qualifier("SKDParkingDAO") ParkingDAO skdParkingDAO) {
        this.iParkingDAO = iParkingDAO;
        this.skdParkingDAO = skdParkingDAO;
    }

    public List<Parking> getParkingState() throws IOException {
        if(parkingStateQualifies(parkingState)){
            parkingState = getProcessedParkingInfo();
        }
        return parkingState;
    }

    public List<ParkingWithHistory> getParkingWithHistory() {
        if(parkingStateWithHistoryQualifiesForUpdate(parkingWithHistoryState)){
            parkingWithHistoryState = getRawParkingInfo();

        }
        return parkingWithHistoryState;
    }

    private boolean parkingStateQualifies(List<Parking> parkingState){
       return parkingState == null || parkingState.isEmpty() || parseUpdateTime(parkingState.get(0).getLastUpdate())
               .isBefore(ParkingDateUtils.getDateTimeInPoland().minusMinutes(1));
    }

    private boolean parkingStateWithHistoryQualifiesForUpdate(List<ParkingWithHistory> parkingWithHistoryState){
        return parkingWithHistoryState == null || parkingWithHistoryState.isEmpty() || parseUpdateTime(parkingWithHistoryState.get(0).getLastUpdate()).isBefore(
                ParkingDateUtils.getDateTimeInPoland().minusMinutes(5)
        );
    }

    private LocalDateTime parseUpdateTime(String lastUpdate){
        return LocalDateTime.parse(lastUpdate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private List<Parking> getProcessedParkingInfo(){
        try{
            return iParkingDAO.getProcessedParkingInfo();
        } catch (Throwable t) {
            return getSkdProcessedParkingInfo();
        }
    }

    private List<ParkingWithHistory> getRawParkingInfo(){
        try{
            return iParkingDAO.getRawParkingData();
        } catch (Throwable t){
            return getSkdRawParkingInfo();
        }
    }

    private List<ParkingWithHistory> getSkdRawParkingInfo(){
        try {
            return skdParkingDAO.getRawParkingData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Parking> getSkdProcessedParkingInfo(){
        try {
            return skdParkingDAO.getProcessedParkingInfo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
