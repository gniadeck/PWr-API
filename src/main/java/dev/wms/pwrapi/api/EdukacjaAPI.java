package dev.wms.pwrapi.api;

import java.io.IOException;
import java.util.List;

import dev.wms.pwrapi.entity.edukacja.Subject;
import dev.wms.pwrapi.scrapper.edukacja.EduScrapperServices;
import dev.wms.pwrapi.service.edukacja.EduService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/edukacja", produces = "application/json")
public class EdukacjaAPI {

    private EduService edukacjaService;

    @Autowired
    public EdukacjaAPI(EduService edukacjaService){
        this.edukacjaService = edukacjaService;
    }

    @GetMapping("/wektor")
    @Operation(summary = "Return available group for user's most recent enrollments",
            description = "This endpoint is probably going to be deprecated, because of migration of enrollment system to USOS. " +
                    "It's implementation is slow and based on Selenium, rather than scraping HTTP requests")
    public ResponseEntity<List<Subject>> getAvailableGroups(@RequestParam("login") String login, @RequestParam("password") String password ) throws IOException {
        List<Subject> response = edukacjaService.doFetchSubjects(login, password);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    @Operation(summary = "Checks if login and password can be used to login to edukacja.cl",
            description = "Just a simple endpoint. You can use it to validate data and save it for later")
    public void loginToEdukacja(@RequestParam("login") String login, @RequestParam("password") String password) throws IOException{
        EduScrapperServices.fetchHTMLConnectionDetails(login, password);
    }
}
