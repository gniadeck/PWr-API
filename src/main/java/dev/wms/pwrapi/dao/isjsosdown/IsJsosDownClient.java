package dev.wms.pwrapi.dao.isjsosdown;

import dev.wms.pwrapi.dto.isjsosdown.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@FeignClient(name = "IsJsosDown", url = "${isjsosdown.service-url}", path = "api/isjsosdown")
public interface IsJsosDownClient {

    @GetMapping("/initial-stats")
    public InitialStatsDTO getInitialStats();

    @GetMapping("/additional-stats/{serviceName}")
    public AdditionalStatsDTO getAdditionalStats(@PathVariable String serviceName);

    @GetMapping("/additional-stats/h2h")
    public HeadToHeadDTO getHeadToHead(@RequestParam List<String> services);

    @GetMapping("/additional-stats/ranking")
    public List<DowntimeStatsRankingDTO> getDowntimeRanking(
            @SpringQueryMap Map<String, LocalDate> optionalDates,
            @RequestParam(defaultValue = "True") Boolean descendingOrder);

    @GetMapping(value = "/csv-data", produces = "text/csv")
    public ResponseEntity<String> getDowntimeDataCSV();

    @GetMapping("/tracked-service/all")
    public List<TrackedServiceDTO> getAllServices();

    @PostMapping("/tracked-service")
    public TrackedServiceDTO addService(@RequestBody ServiceDTO service);

    @PutMapping("/tracked-service/{serviceId}")
    public TrackedServiceDTO updateServiceById(@PathVariable int serviceId, @RequestBody TrackedServiceDTO trackedService);

}
