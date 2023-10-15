package dev.wms.pwrapi.api;


import dev.wms.pwrapi.dto.events.EventDto;
import dev.wms.pwrapi.service.events.EventsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping(value = "api/events")
@RequiredArgsConstructor
public class EventsAPI {

    private final EventsService eventsService;


    @GetMapping
    @Operation(summary = "returns list of events in PWr from given month and year (both optional)",
    description = "endpoint gets events from site: https://pwr.edu.pl/uczelnia/przed-nami with given month and year" +
            " By default month and year are current. When events are duplicated, it returns it merged")
    public ResponseEntity<Set<EventDto>> getEventsOfTheMonth(
            @RequestParam Optional<Month> month, @RequestParam Optional<Integer> year){
        return ResponseEntity.ok(eventsService.getEventsOfMonth(month, year));
    }

}
