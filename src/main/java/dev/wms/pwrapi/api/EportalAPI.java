package dev.wms.pwrapi.api;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import dev.wms.pwrapi.entity.eportal.MarkSummary;
import dev.wms.pwrapi.entity.eportal.calendar.CalendarMonth;
import dev.wms.pwrapi.dto.eportal.sections.EportalSection;
import dev.wms.pwrapi.service.eportal.EportalService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.wms.pwrapi.utils.eportal.exceptions.WrongCourseIdException;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/eportal", produces = "application/json")
public class EportalAPI {

    private EportalService eService;
    
    @Autowired
    public EportalAPI(EportalService eService){
        this.eService = eService;
    }

    @GetMapping()
    @Operation(summary = "Validates ePortal login data", description = "Can be used to cache login and password for later API usage")
    public void getEportalData(@RequestParam String login, @RequestParam String password) throws JsonProcessingException, IOException, LoginException {
        eService.getEportalData(login, password);
    }

    @GetMapping("/kursy")
    @Operation(summary = "Returns all courses of the given user")
    public ResponseEntity<String> getEportalKursy(@RequestParam String login, @RequestParam String password) throws IOException, LoginException {

        String result = eService.getEportalKursy(login, password);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/kurs/{id}/sekcje")
    @Operation(summary = "Returns all sections for the given course", description = "You can fetch the course ID using /kursy endpoint")
    public ResponseEntity<List<EportalSection>> getEportalSekcje(@RequestParam String login, @RequestParam String password, @PathVariable int id) throws JsonProcessingException, IOException, LoginException, WrongCourseIdException {

        return ResponseEntity.status(HttpStatus.OK).body(eService.getEportalSekcje(login, password, id));
    }


    @GetMapping("/kursy/{id}/oceny")
    @Operation(summary = "Returns all marks for the given course", description = "You can fetch the course ID using /kursy endpoint")
    public ResponseEntity<List<MarkSummary>> getEportalOceny(@RequestParam String login, @RequestParam String password, @PathVariable int id) throws JsonProcessingException {

        return ResponseEntity.status(HttpStatus.OK).body(eService.getEportalOceny(login, password, id));
    }


    @GetMapping("/kalendarz")
    @Operation(summary = "Returns events that take place in month with offset", description = "Max offset is from -10 to 10")
    public ResponseEntity<CalendarMonth> getEportalKalendarzMiesiac(@RequestParam String login, @RequestParam String password,
                                                                    @RequestParam(defaultValue = "0") @Min(-10) @Max(10) int offset) throws IOException {

        return ResponseEntity.status(HttpStatus.OK).body(eService.getEportalKalendarzOffset(login, password, offset));
    }


    @GetMapping("/kalendarz/pobierz")
    @Operation(summary = "Returns calendar in ICS format", description = "Calendar range is 60 days back and forth")
    public ResponseEntity<String> getEportalICS(@RequestParam String login, @RequestParam String password) throws IOException {

        return ResponseEntity.status(HttpStatus.OK).body(eService.getEportalKalendarzIcsLink(login, password));
    }

}
