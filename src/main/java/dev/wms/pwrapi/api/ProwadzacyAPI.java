package dev.wms.pwrapi.api;

import dev.wms.pwrapi.entity.prowadzacy.ProwadzacyResult;
import dev.wms.pwrapi.service.prowadzacy.ProwadzacyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/prowadzacy", produces="application/json")
@Validated
public class ProwadzacyAPI {

    private final ProwadzacyService prowadzacyService;

    @Autowired
    public ProwadzacyAPI(ProwadzacyService prowadzacyService){
        this.prowadzacyService = prowadzacyService;
    }

    @GetMapping
    @Operation(summary = "Returns OK if website is reachable", description = "Mostly use for debugging issues")
    public void getWebsiteStatus(){
        prowadzacyService.getWebsiteStatus();
    }

    @GetMapping("/szukaj")
    @Operation(summary = "Returns teacher's plan",
            description = "Use query parameter to look for teacher you want to get plan of. " +
                    "Please remember that we take only best matching result. For example if you want to " +
                    "look for \"Dariusz Konieczny\", and you only query \"Konieczny\" you might end with result for " +
                    "\"Jan Skonieczny\". query parameter is case-insensitive. You can also use offset value, " +
                    "which returns you teacher's plan few weeks back and forth")
    public ResponseEntity<ProwadzacyResult> getForTeacherQuery(@RequestParam String query,
                                                               @RequestParam(defaultValue = "0") @Min(-20) @Max(20) int offset){
        return ResponseEntity.status(HttpStatus.OK).body(prowadzacyService.getForTeacherQuery(query, offset));
    }

    @GetMapping("/szukaj/sala")
    @Operation(summary = "Returns room's plan", description = "In order to use this endpoint you must provide a " +
            "very precise query. Please remember that building format is usually %LETTER%-%NUMBER% (for example D-20) Parameters are case-insensitive")

    public ResponseEntity<ProwadzacyResult> getForRoomQuery(@RequestParam String building,
                                                            @RequestParam String room){
        return ResponseEntity.status(HttpStatus.OK).body(prowadzacyService.getForRoomQuery(building, room, null));
    }

    @GetMapping("/szukaj/przedmiot")
    @Operation(summary = "Return's plan of all lessons in given course", description = "In order to use this endpoint you must provide a " +
            "very precise query. Parameters are case-insensitive, but must contain full subject name. For example query for \"Algorytmy i struktury dan\" would result in NoResultsException, or wrong results")
    public ResponseEntity<ProwadzacyResult> getForSubjectQuery(@RequestParam String query){
        return ResponseEntity.status(HttpStatus.OK).body(prowadzacyService.getForSubjectQuery(query, null));
    }

}
