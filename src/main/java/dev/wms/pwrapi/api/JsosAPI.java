package dev.wms.pwrapi.api;

import java.io.IOException;
import java.util.List;

import dev.wms.pwrapi.entity.jsos.JsosLesson;
import dev.wms.pwrapi.entity.jsos.JsosStudentData;
import dev.wms.pwrapi.entity.jsos.finance.FinanceResult;
import dev.wms.pwrapi.entity.jsos.finance.operations.FinanceOperationResult;
import dev.wms.pwrapi.entity.jsos.marks.JsosSemester;
import dev.wms.pwrapi.entity.jsos.messages.JsosMessageFull;
import dev.wms.pwrapi.entity.jsos.messages.JsosMessageShort;
import dev.wms.pwrapi.entity.jsos.weeks.JsosDay;
import dev.wms.pwrapi.entity.jsos.weeks.JsosWeek;
import dev.wms.pwrapi.service.jsos.JsosService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import dev.wms.pwrapi.utils.jsos.exceptions.NoTodayClassException;
import dev.wms.pwrapi.utils.jsos.exceptions.TooBigOffsetException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/jsos", produces = "application/json")
@Validated
public class JsosAPI {

    private JsosService jsosService;

    @Autowired
    public JsosAPI(JsosService jsosService){
        this.jsosService = jsosService;
    }

    @GetMapping("/wiadomosci/{page}/tresc")
    @Operation(summary = "Returns full messages with given id from one page")
    public ResponseEntity<List<JsosMessageFull>> getStudentsMessage(String login, String password, @PathVariable @Min(1) int page, Integer... ids) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(jsosService.getStudentMessage(login, password, page, ids));
    }

    @GetMapping("/wiadomosci/{page}")
    @Operation(summary = "Return all students messages from the given page")
    public ResponseEntity<List<JsosMessageShort>> getStudentsMessages(String login, String password, @PathVariable @Min(1) int page) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(jsosService.getStudentMessagesList(login, password, page));
    }

    @GetMapping("/zajecia/tydzien/offset/{offset}")
    @Operation(summary = "Get lessons for whole week with given offset",
            description = "You can use this endpoint to get all lessons in given week, up to 10 weeks back and forth. " +
                    "For higher limits you need to host the API yourself and change it in code")
    public ResponseEntity<JsosWeek> getOffsetWeekLessons(String login, String password, @PathVariable @Min(-10) @Max(10) int offset) throws IOException, TooBigOffsetException, LoginException {
        return ResponseEntity.status(HttpStatus.OK).body(jsosService.getOffsetLessons(login, password, offset));
    }


    @GetMapping("/zajecia/tydzien/nastepny")
    @Operation(summary = "Returns next week's lessons", description = "Equal to /zajecia/tydzien/offset/{1}")
    public ResponseEntity<JsosWeek> getNextWeekLessons(String login, String password) throws IOException, LoginException {
        return ResponseEntity.status(HttpStatus.OK).body(jsosService.getNextWeekLessons(login, password));

    }

    @GetMapping("/zajecia/tydzien")
    @Operation(summary = "Returns this week's lessons", description = "Equal to /zajecia/tydzien/offset/{0}")
    public ResponseEntity<JsosWeek> getThisWeekLessons(String login, String password) throws IOException, LoginException {
        return ResponseEntity.status(HttpStatus.OK).body(jsosService.getThisWeekLessons(login, password));
    }

    @GetMapping("/zajecia/jutro")
    @Operation(summary = "Returns tommorow's lessons",
            description = "Be careful with this endpoint since JSOS is pretty unstable when handling holiday days for example. Please open issues if you will spot the bug")
    public ResponseEntity<JsosDay> getTommorowLessons(String login, String password) throws IOException, LoginException {
        return ResponseEntity.status(HttpStatus.OK).body(jsosService.getTomorrowLessons(login, password));

    }


    @GetMapping("/zajecia/dzis")
    @Operation(summary = "Return today's lesons", description = "Yeah, that's all")
    public ResponseEntity<JsosDay> getTodaysLessons(String login, String password) throws IOException, LoginException, NoTodayClassException {
         return ResponseEntity.status(HttpStatus.OK).body(jsosService.getTodaysLessions(login, password));
    }


    @GetMapping("/zajecia")
    @Operation(summary = "Returns all students lessons", description = "Returns all student courses that he has signed to.")
    public ResponseEntity<List<JsosLesson>> getAllStudentsLessons(String login, String password) throws IOException, LoginException {
        return ResponseEntity.status(HttpStatus.OK).body(jsosService.getAllStudentsLessons(login, password));
    }

    @GetMapping("/oceny")
    @Operation(summary = "Returns student's marks from all semesters")
    public ResponseEntity<List<JsosSemester>> getStudentMarks(String login, String password)
            throws LoginException, IOException {

        return ResponseEntity.status(HttpStatus.OK).body(jsosService.getStudentMarks(login, password));

    }

    @GetMapping
    @Operation(summary = "Logins to JSOS and validates the data", description = "Can be used to cache login data for later API usage")
    public void login(@RequestParam("login") String login, @RequestParam("password") String password)
            throws LoginException {
        jsosService.login(login, password);
    }

    @GetMapping("/dane")
    @Operation(summary = "Returns student data like name, surname etc.")
    public ResponseEntity<JsosStudentData> getDane(@RequestParam("login") String login,
                                                   @RequestParam("password") String password) throws LoginException {
        return ResponseEntity.status(HttpStatus.OK).body(jsosService.getStudentData(login, password));

    }


    @GetMapping("/finanse")
    @Operation(summary = "Return student financial information (paid and unpaid stuff)")
    public ResponseEntity<FinanceResult> getFinanse(@RequestParam("login") String login,
                                                    @RequestParam("password") String password) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(jsosService.getStudentFinanse(login, password));
    }

    @GetMapping("/finanse/operacje")
    @Operation(summary = "Returns all operations registered on student's internal bank account")
    public ResponseEntity<FinanceOperationResult> getFinanceOperations(@RequestParam("login") String login,
                                                                       @RequestParam("password") String password) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(jsosService.getStudentFinanceOperations(login, password));
    }

}