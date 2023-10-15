package dev.wms.pwrapi.api;

import dev.wms.pwrapi.dto.library.LibraryTitle;
import dev.wms.pwrapi.service.library.LibraryService;
import dev.wms.pwrapi.utils.common.PageRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class LibraryAPI {

    private final LibraryService libraryService;

    @GetMapping("/search")
    @Operation(description = "Returns results from Politechnika's Primo VE search engine")
    public ResponseEntity<List<LibraryTitle>> searchFor(String query,
                                                        @RequestParam(value = "limit", defaultValue = "10") @Min(0) @Max(20) int limit,
                                                        @RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset){
        return ResponseEntity.ok(libraryService.searchFor(query, PageRequest.of(limit, offset)));
    }

}
