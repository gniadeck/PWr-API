package dev.wms.pwrapi.api;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import dev.wms.pwrapi.dto.parking.Parking;
import dev.wms.pwrapi.dto.parking.ParkingWithHistory;
import dev.wms.pwrapi.service.parking.ParkingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/api/parking", produces = "application/json")
@AllArgsConstructor
public class ParkingAPI {

    private ParkingService parkingService;

    @GetMapping
    @Operation(summary = "Returns processed data from skd.pwr.edu.pl", description = "You can use it to get data from skd.pwr.edu.pl in simple format")
    public ResponseEntity<List<Parking>> getProcessedParkingInfo() throws IOException {
        List<Parking> result = parkingService.getParkingData();
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @GetMapping("/raw")
    @Operation(summary = "Returns history data from skd.pwr.edu.pl",
            description = "You can use it to get parking history data from last 24h")
    public ResponseEntity<List<ParkingWithHistory>> getRawParkingInfo() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(parkingService.getRawParkingData());
    }


}
