package dev.wms.pwrapi.api;

import dev.wms.pwrapi.dto.isjsosdown.*;
import dev.wms.pwrapi.service.isjsosdown.IsJsosDownService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/isjsosdown")
@RequiredArgsConstructor
public class IsJsosDownAPI {

    private final IsJsosDownService isJsosDownService;

    @GetMapping("/initial-stats")
    public ResponseEntity<InitialStatsDTO> getInitialStats() {
        return ResponseEntity.ok(isJsosDownService.getInitialStats());
    }

    @GetMapping("/additional-stats/{serviceName}")
    public ResponseEntity<AdditionalStatsDTO> getAdditionalStats(@PathVariable String serviceName) {
        return ResponseEntity.ok(isJsosDownService.getAdditionalStats(serviceName));
    }

    @GetMapping("/additional-stats/h2h")
    public ResponseEntity<HeadToHeadDTO> getHeadToHead(@RequestParam List<String> services) {
        return ResponseEntity.ok(isJsosDownService.getHeadToHead(services));
    }

    @GetMapping("/additional-stats/ranking")
    public ResponseEntity<List<DowntimeStatsRankingDTO>> getDowntimeRanking(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate,
            @RequestParam(defaultValue = "True") Boolean descendingOrder) {
        return ResponseEntity.ok(isJsosDownService.getDowntimeRanking(startDate, endDate, descendingOrder));
    }

    @GetMapping("/csv-data")
    public ResponseEntity<String> getDowntimeDataCSV() {
        return isJsosDownService.getDowntimeDataCSV();
    }

    @GetMapping("/tracked-service/all")
    public ResponseEntity<List<TrackedServiceDTO>> getAllServices(){
        return ResponseEntity.ok(isJsosDownService.getAllServices());
    }

    @PostMapping("/tracked-service")
    public ResponseEntity<TrackedServiceDTO> addService(@RequestBody ServiceDTO service) {
        return ResponseEntity.ok(isJsosDownService.addService(service));
    }

    @PutMapping("/tracked-service/{serviceId}")
    public ResponseEntity<TrackedServiceDTO> updateServiceById(
            @PathVariable int serviceId, @RequestBody TrackedServiceDTO trackedService) {
        return ResponseEntity.ok(isJsosDownService.updateServiceById(serviceId, trackedService));
    }

}
