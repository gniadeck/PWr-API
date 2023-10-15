package dev.wms.pwrapi.service.isjsosdown;

import dev.wms.pwrapi.dao.isjsosdown.IsJsosDownClient;
import dev.wms.pwrapi.dto.isjsosdown.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IsJsosDownService {

    private final IsJsosDownClient isJsosDownClient;

    public InitialStatsDTO getInitialStats() {
        return isJsosDownClient.getInitialStats();
    }

    public AdditionalStatsDTO getAdditionalStats(String serviceName) {
        return isJsosDownClient.getAdditionalStats(serviceName);
    }

    public HeadToHeadDTO getHeadToHead(List<String> services) {
        return isJsosDownClient.getHeadToHead(services);
    }

    public List<DowntimeStatsRankingDTO> getDowntimeRanking(Optional<LocalDate> optionalStartDate,
                                                            Optional<LocalDate> optionalEndDate,
                                                            Boolean descendingOrder) {
        Map<String, LocalDate> optionalDates = new HashMap<>();
        optionalStartDate.ifPresent(startDate -> optionalDates.put("startDate", startDate));
        optionalEndDate.ifPresent(endDate -> optionalDates.put("endDate", endDate));
        return isJsosDownClient.getDowntimeRanking(optionalDates, descendingOrder);
    }

    public ResponseEntity<String> getDowntimeDataCSV() {
        return isJsosDownClient.getDowntimeDataCSV();
    }

    public List<TrackedServiceDTO> getAllServices() {
        return isJsosDownClient.getAllServices();
    }

    public TrackedServiceDTO addService(ServiceDTO service) {
        return isJsosDownClient.addService(service);
    }

    public TrackedServiceDTO updateServiceById(int serviceId, TrackedServiceDTO trackedService) {
        return isJsosDownClient.updateServiceById(serviceId, trackedService);
    }
}
