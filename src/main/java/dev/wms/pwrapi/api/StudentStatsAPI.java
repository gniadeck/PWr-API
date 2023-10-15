package dev.wms.pwrapi.api;

import dev.wms.pwrapi.model.studentStats.StudentStatsData;
import dev.wms.pwrapi.service.studentStats.StudentStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/studentStats")
@RequiredArgsConstructor
@Validated
public class StudentStatsAPI {

    private final StudentStatsService studentStatsService;

    @GetMapping("/getStaticMockedData")
    @Operation(summary = "Returns static mocked data [DEBUG] TEST FOR CI")
    @Deprecated(forRemoval = true)
    public ResponseEntity<StudentStatsData> getStaticMockedData(
            @Parameter(name = "Accept-Language", in = ParameterIn.HEADER, schema = @Schema(type = "string", allowableValues = {"pl", "en"}))
            @RequestHeader(name = "Accept-Language", required = false) String language){

        return ResponseEntity.ok().body(studentStatsService.getStaticMockedData());
    }

    @PostMapping
    @Operation(summary = "Get student stats")
    public ResponseEntity<StudentStatsData> getStudentStatsData(
            String login, String password,
            @Parameter(name = "Accept-Language", in = ParameterIn.HEADER, schema = @Schema(type = "string", allowableValues = {"pl", "en"}))
            @RequestHeader(name = "Accept-Language", required = false) String language){

        return ResponseEntity.ok().body(studentStatsService.get(login, password));
    }

    @GetMapping("/getDynamicMockedData/{numOfBlocks}")
    @Operation(summary = "Returns dynamic mocked data [DEBUG] (number of objects in result is random, and values are also dynamic). " +
            "Parameter numOfBlocks allows to get exact number of content as it needed [0-100], but to num over 28 cards may occur duplications")
    @Deprecated(forRemoval = true)
    public ResponseEntity<StudentStatsData> getDynamicMockedData(
            @PathVariable @Min(0) @Max(100) Integer numOfBlocks,
            @Parameter(name = "Accept-Language", in = ParameterIn.HEADER, schema = @Schema(type = "string", allowableValues = {"pl", "en"}))
            @RequestHeader(name = "Accept-Language", required = false) String language){

        return ResponseEntity.ok().body(studentStatsService.getDynamicMockedData(numOfBlocks));
    }
}
