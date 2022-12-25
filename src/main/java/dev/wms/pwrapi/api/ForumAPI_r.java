package dev.wms.pwrapi.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.entity.forum.Review_r;
import dev.wms.pwrapi.entity.forum.Teacher;
import dev.wms.pwrapi.entity.forum.TeacherWithReviewsDTO;
import dev.wms.pwrapi.service.forum.ForumService_r;
import dev.wms.pwrapi.utils.forum.consts.Category;
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
    public ResponseEntity<Review_r> getReviewById(@PathVariable @Positive(message = "reviewId has to be >= 0") Long reviewId) {
        return ResponseEntity.of(forumService.getReviewById(reviewId));
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
    public ResponseEntity<List<Teacher>> getTeachersByCategory(@PathVariable Category category) {
        /*
        List<Teacher> response = forumService.getTeachersByCategory(category);
        if(response.isEmpty()){
            throw new CategoryMembersNotFoundException(category);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
         */
        return null;
    }
}
