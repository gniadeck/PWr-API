package dev.wms.pwrapi.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.entity.forum.Review_r;
import dev.wms.pwrapi.entity.forum.Teacher;
import dev.wms.pwrapi.entity.forum.TeacherInfoDTO;
import dev.wms.pwrapi.entity.forum.TeacherWithReviewsDTO;
import dev.wms.pwrapi.service.forum.ForumService_r;
import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO_r;
import dev.wms.pwrapi.utils.forum.exceptions.CategoryMembersNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

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
    @Operation(summary = "Returns total number of teacher reviews collected.")
    public ResponseEntity<DatabaseMetadataDTO_r> getTotalReviews() {
        return ResponseEntity.ok(forumService.getTotalReviews());
    }

    @GetMapping("/opinie/{reviewId}")
    @Operation(summary = "Returns review with specified reviewId.")
    public ResponseEntity<Review_r> getReviewById(@PathVariable @Positive(message = "reviewId has to be >= 0") Long reviewId) {
        return ResponseEntity.ok(forumService.getReviewById(reviewId));
    }

    @GetMapping("/prowadzacy")
    @Operation(summary = "Returns total number of teachers.")
    public ResponseEntity<DatabaseMetadataDTO_r> getTotalTeachers() {
        return ResponseEntity.ok(forumService.getTotalTeachers());
    }

    @GetMapping(value = "/prowadzacy/{teacherId}")
    @Operation(summary = "Returns teacher with specified id.")
    public ResponseEntity<TeacherWithReviewsDTO> getTeacherWithReviews(@PathVariable @Positive (message = "teacherId has to be >= 0")
                                                             Long teacherId) {
        return ResponseEntity.ok(forumService.getTeacherWithAllReviewsById(teacherId));
    }

    @GetMapping("/prowadzacy/szukajId")
    @Operation(summary = "Returns certain teacher specified by id and limited number of reviews.",
            description = "Maximal number of fetched reviews is specified by the limit parameter, set limit = -1 to " +
                    "fetch all available reviews.")
    public ResponseEntity<TeacherWithReviewsDTO> getTeacherWithLimitedReviewsById(
                                @RequestParam("teacherId") @Positive (message = "teacherId has to be >= 0") Long teacherId,
                                @RequestParam("limit") @Min(value = -1, message = "limit has to be >= -1") Long limit) {
        return ResponseEntity.ok(forumService.getTeacherWithLimitedReviewsById(teacherId, limit));
    }

    @GetMapping("/prowadzacy/szukajImie")
    @Operation(summary = "Returns certain teacher specified by full name and limited number of reviews.",
            description = "Parameters firstName and lastName are interchangeable, query is based on pattern matching. " +
                    "Maximal number of reviews is specified by the limit parameter, set limit = -1 to fetch all available reviews.")
    public ResponseEntity<TeacherWithReviewsDTO> getTeacherWithLimitedReviewsByFullName(
                                @RequestParam("firstName") @NotNull(message = "firstName is required") String firstName,
                                @RequestParam("lastName") @NotNull(message = "lastName is required") String lastName,
                                @RequestParam("limit") @Min(value = -1, message = "limit has to be >= -1") Long limit) {
        /*
        if(limit >= -1){
            try {
                Teacher response = forumService.fetchLimitedTeacherReviewsByFullName(firstName, lastName, limit);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }catch(EmptyResultDataAccessException e){
                throw new TeacherNotFoundByFullNameException(firstName, lastName);
            }
        }else{
            throw new InvalidLimitException(limit);
        }
        */
        return ResponseEntity.ok(forumService.getTeacherWithLimitedReviewsByFullName(firstName, lastName, limit));
    }

    @GetMapping("/prowadzacy/kategoria/{category}")
    @Operation(summary = "Returns all teachers who belong to the specified category.")
    public ResponseEntity<Set<TeacherInfoDTO>> getTeachersByCategory(
                                            @PathVariable @NotNull(message = "category is required") String category) {
        /*
        List<Teacher> response = forumService.getTeachersByCategory(category);
        if(response.isEmpty()){
            throw new CategoryMembersNotFoundException(category);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
         */
        return ResponseEntity.ok(forumService.getTeachersInfoByCategory(category));
    }

    @GetMapping("/prowadzacy/ranking")
    @Operation(summary = "Returns teachers who belong to the specified category ranked by their average rating.")
    public ResponseEntity<Set<TeacherInfoDTO>> getTeachersRankedByCategory(@RequestParam("kategoria") String category) {
        /*
        List<Teacher> response = forumService.getBestTeachersRankedByCategory(category);
        if(response.isEmpty()){
            throw new CategoryMembersNotFoundException(category);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
        */
        return ResponseEntity.ok(forumService.getBestTeachersOfCategory(category));
    }

    // TODO -> fix limit
    @GetMapping("/prowadzacy/{category}/ranking/najlepsi")
    @Operation(summary = "Returns limited number of best rated teachers who belong to the specified category, example reviews are provided.",
            description = "Number of return teachers is specified by the limit parameter, each teacher has a maximal example of three associated reviews.")
    public ResponseEntity<Set<TeacherWithReviewsDTO>> getBestRankedTeachersByCategoryWithReviewsLimited(
            @PathVariable String category, @RequestParam("limit") @Min(-1) Long limit) {

        /*
        List<Teacher> response = forumService.getBestRankedTeachersByCategoryLimited(category, limit);
        if(response.isEmpty()){
            throw new CategoryMembersNotFoundException(category);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
        */
        return ResponseEntity.ok(forumService.getLimitedBestTeachersOfCategoryWithExampleReviews(category, limit));
    }

    // TODO -> fix limit
    @GetMapping("/prowadzacy/{category}/ranking/najgorsi")
    @Operation(summary = "Returns limited number of worst rated teachers who belong to the specified category, example reviews are provided.",
            description = "Number of return teachers is specified by the limit parameter, each teacher has a maximal example of three associated reviews.")
    public ResponseEntity<Set<TeacherWithReviewsDTO>> getWorstRankedTeachersByCategoryWithReviewsLimited(
            @PathVariable String category, @RequestParam("limit") @Min(-1) Long limit) {
        /*
        List<Teacher> response = forumService.getWorstRankedTeachersByCategoryLimited(category, limit);
        if(response.isEmpty()){
            throw new CategoryMembersNotFoundException(category);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
        */
        return ResponseEntity.ok(forumService.getLimitedWorstTeachersOfCategoryWithExampleReviews(category, limit));
    }
}
