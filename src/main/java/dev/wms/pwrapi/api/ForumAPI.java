package dev.wms.pwrapi.api;

import dev.wms.pwrapi.entity.forum.Review;
import dev.wms.pwrapi.entity.forum.TeacherInfoDTO;
import dev.wms.pwrapi.entity.forum.TeacherWithReviewsDTO;
import dev.wms.pwrapi.service.forum.ForumServiceImpl;
import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@RestController
@RequestMapping("/api/forum")
@RequiredArgsConstructor
@Validated
public class ForumAPI {

    private final ForumServiceImpl forumService;

    @Cacheable("metadata")
    @GetMapping
    @Operation(summary = "Returns database metadata such as number of records in each category and latest refresh timestamp.")
    public ResponseEntity<DatabaseMetadataDTO> getDatabaseMetadata() {
        return ResponseEntity.ok(forumService.getDatabaseMetadata());
    }

    @Cacheable("reviews")
    @GetMapping("/opinie")
    @Operation(summary = "Returns total number of teacher reviews collected.")
    public ResponseEntity<DatabaseMetadataDTO> getTotalReviews() {
        return ResponseEntity.ok(forumService.getTotalReviews());
    }

    // TODO -> emtpy return instead of exception in case review is not found
    @GetMapping("/opinie/{reviewId}")
    @Operation(summary = "Returns review with specified reviewId.")
    public ResponseEntity<Review> getReviewById(@PathVariable @Positive(message = "reviewId powinno być >= 0") Long reviewId) {
        return ResponseEntity.ok(forumService.getReviewById(reviewId));
    }

    @Cacheable("teachers")
    @GetMapping("/prowadzacy")
    @Operation(summary = "Returns total number of teachers.")
    public ResponseEntity<DatabaseMetadataDTO> getTotalTeachers() {
        return ResponseEntity.ok(forumService.getTotalTeachers());
    }

    @GetMapping(value = "/prowadzacy/{teacherId}")
    @Operation(summary = "Returns teacher with specified id.")
    public ResponseEntity<TeacherWithReviewsDTO> getTeacherWithReviews(
                                        @PathVariable @Positive (message = "teacherId powinno być >= 0") Long teacherId) {
        return ResponseEntity.ok(forumService.getTeacherWithAllReviewsById(teacherId));
    }

    @GetMapping("/prowadzacy/szukajId")
    @Operation(summary = "Returns certain teacher specified by id and limited number of reviews.",
            description = "Maximal number of fetched reviews is specified by the limit parameter, set limit = -1 to " +
                    "fetch all available reviews.")
    public ResponseEntity<TeacherWithReviewsDTO> getTeacherWithLimitedReviewsById(
                                @RequestParam("teacherId") @Positive (message = "teacherId powinno być >= 0") Long teacherId,
                                @RequestParam("limit") @Min(value = -1, message = "limit powinien być >= -1") Long limit) {
        return ResponseEntity.ok(forumService.getTeacherWithLimitedReviewsById(teacherId, limit));
    }

    @GetMapping("/prowadzacy/szukajImie")
    @Operation(summary = "Returns certain teacher specified by full name and limited number of reviews.",
            description = "Parameters firstName and lastName are interchangeable, query is based on pattern matching. " +
                    "Maximal number of reviews is specified by the limit parameter, set limit = -1 to fetch all available reviews.")
    public ResponseEntity<TeacherWithReviewsDTO> getTeacherWithLimitedReviewsByFullName(
                                @RequestParam("firstName") @NotNull(message = "firstName jest wymagane") String firstName,
                                @RequestParam("lastName") @NotNull(message = "lastName jest wymagane") String lastName,
                                @RequestParam("limit") @Min(value = -1, message = "limit powinien być >= -1") Long limit) {
        return ResponseEntity.ok(forumService.getTeacherWithLimitedReviewsByFullName(firstName, lastName, limit));
    }

    @GetMapping("/prowadzacy/kategoria/{category}")
    @Operation(summary = "Returns all teachers who belong to the specified category.")
    public ResponseEntity<Set<TeacherInfoDTO>> getTeachersByCategory(
                                            @PathVariable String category) {
        return ResponseEntity.ok(forumService.getTeachersInfoByCategory(category));
    }

    //TODO -> add number of reviews (feature)
    @GetMapping("/prowadzacy/ranking")
    @Operation(summary = "Returns teachers who belong to the specified category ranked by their average rating.")
    public ResponseEntity<Set<TeacherInfoDTO>> getTeachersRankedByCategory(
                                                                    @RequestParam(name = "kategoria") String category) {
        return ResponseEntity.ok(forumService.getBestTeachersOfCategory(category));
    }

    @GetMapping("/prowadzacy/{category}/ranking/najlepsi")
    @Operation(summary = "Returns limited number of best rated teachers who belong to the specified category, " +
            "example reviews are provided.",
            description = "Number of return teachers is specified by the limit parameter, each teacher has a maximal " +
                    "example of three associated reviews.")
    public ResponseEntity<Set<TeacherWithReviewsDTO>> getBestRankedTeachersByCategoryWithReviewsLimited(
            @PathVariable String category, @RequestParam("limit") @Positive(message = "limit powinien być >= 0") int limit) {
        return ResponseEntity.ok(forumService.getLimitedBestTeachersOfCategoryWithExampleReviews(category, limit));
    }

    @GetMapping("/prowadzacy/{category}/ranking/najgorsi")
    @Operation(summary = "Returns limited number of worst rated teachers who belong to the specified category, example reviews are provided.",
            description = "Number of return teachers is specified by the limit parameter, each teacher has a maximal example of three associated reviews.")
    public ResponseEntity<Set<TeacherWithReviewsDTO>> getWorstRankedTeachersByCategoryWithReviewsLimited(
            @PathVariable String category, @RequestParam("limit") @Positive(message = "limit powinien być >= 0") int limit) {
        return ResponseEntity.ok(forumService.getLimitedWorstTeachersOfCategoryWithExampleReviews(category, limit));
    }
}
