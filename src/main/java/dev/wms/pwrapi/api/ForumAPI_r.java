package dev.wms.pwrapi.api;

import dev.wms.pwrapi.entity.forum.Review_r;
import dev.wms.pwrapi.service.forum.ForumService_r;
import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO_r;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/forum_r")
@AllArgsConstructor
public class ForumAPI_r {

    private final ForumService_r forumService;

    @GetMapping
    @Operation(summary = "Returns database metadata such as number of records in each category and latest refresh timestamp.")
    public ResponseEntity<DatabaseMetadataDTO_r> getDatabaseMetadata() {
        return ResponseEntity.ok(forumService.getDatabaseMetadata());
    }

    @GetMapping("/opinie")
    @Operation(summary = "Returns total number of reviews.")
    public ResponseEntity<DatabaseMetadataDTO_r> getTotalReviews() {
        return ResponseEntity.ok(forumService.getTotalReviews());
    }

    @GetMapping("/opinie/{reviewId}")
    @Operation(summary = "Returns review with specified id.")
    public ResponseEntity<Review_r> getReviewById(@PathVariable @Positive Long reviewId) {
        return ResponseEntity.of(forumService.getReviewById(reviewId));
    }

    @GetMapping("/prowadzacy")
    @Operation(summary = "Returns total number of teachers.")
    public ResponseEntity<DatabaseMetadataDTO_r> getTotalTeachers() {
        return ResponseEntity.ok(forumService.getTotalTeachers());
    }
}
