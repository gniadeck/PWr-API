package dev.wms.pwrapi.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.entity.forum.Review;
import dev.wms.pwrapi.entity.forum.Teacher;
import dev.wms.pwrapi.service.forum.ForumService;
import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO;
import dev.wms.pwrapi.utils.forum.exceptions.*;
import dev.wms.pwrapi.utils.generalExceptions.InvalidIdException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController("/api/forum")
@RequestMapping(value = "/api/forum", produces = "application/json")
public class ForumAPI {

    private final ForumService forumService;

    @Autowired
    public ForumAPI(ForumService forumService){
        this.forumService = forumService;
    }

    @GetMapping
    @Operation(summary = "Returns database metadata such as number of records in each category and latest refresh timestamp.")
    public ResponseEntity<DatabaseMetadataDTO> getDatabaseMetadata() throws JsonProcessingException {
        DatabaseMetadataDTO result = forumService.getDatabaseMetadata();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/opinie")
    @Operation(summary = "Returns total number of reviews.")
    public ResponseEntity<DatabaseMetadataDTO> getTotalReviews() throws JsonProcessingException {
        DatabaseMetadataDTO result = forumService.getTotalReviews();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/opinie/{reviewId}")
    @Operation(summary = "Returns review with specified id.")
    public ResponseEntity<Review> getReviewById(@PathVariable @Min(1) int reviewId) throws JsonProcessingException {
        try {
            Review result = forumService.getReviewById(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }catch (EmptyResultDataAccessException e){
            throw new ReviewNotFoundException((long) reviewId);
        }
    }

    @GetMapping("/prowadzacy")
    @Operation(summary = "Returns total number of teachers.")
    public ResponseEntity<DatabaseMetadataDTO> getTotalTeachers() throws JsonProcessingException {
        DatabaseMetadataDTO result = forumService.getTotalTeachers();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping(value = "/prowadzacy/{teacherId}")
    @Operation(summary = "Returns teacher with specified id.")
    public ResponseEntity<Teacher> fetchAllTeacherReviewsById(@PathVariable int teacherId) throws JsonProcessingException {
        if(teacherId <= 0) {
            throw new InvalidIdException(teacherId);
        }

        try{
            Teacher result = forumService.fetchLimitedTeacherReviewsById(teacherId, -1);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }catch(EmptyResultDataAccessException e){
            throw new TeacherNotFoundByIdException(teacherId);
        }
    }

    @GetMapping("/prowadzacy/szukajId")
    @Operation(summary = "Returns certain teacher specified by id and limited number of reviews.",
        description = "Maximal number of fetched reviews is specified by the limit parameter, set limit = -1 to fetch all available reviews.")
    public ResponseEntity<Teacher> fetchLimitedTeacherReviewsById(@RequestParam("teacherId") int teacherId, @RequestParam("limit") int limit) throws JsonProcessingException {
        if(teacherId <= 0){
            throw new InvalidIdException(teacherId);
        }

        if(limit >= -1) {
            try {
                Teacher response = forumService.fetchLimitedTeacherReviewsById(teacherId, limit);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }catch(EmptyResultDataAccessException e){
                throw new TeacherNotFoundByIdException(teacherId);
            }
        }else{
            throw new InvalidLimitException(limit);
        }
    }

    @GetMapping("/prowadzacy/szukajImie")
    @Operation(summary = "Returns certain teacher specified by full name and limited number of reviews.",
        description = "Parameters firstName and lastName are interchangeable, query is based on pattern matching. " +
                "Maximal number of reviews is specified by the limit parameter, set limit = -1 to fetch all available reviews.")
    public ResponseEntity<Teacher> fetchTeacherReviewsByFullName(@RequestParam("firstName") String firstName,
                                                                 @RequestParam("lastName") String lastName, @RequestParam("limit") int limit) throws JsonProcessingException {
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
    }

    @GetMapping("/prowadzacy/kategoria/{category}")
    @Operation(summary = "Returns all teachers who belong to the specified category.")
    public ResponseEntity<List<Teacher>> getTeachersByCategory(@PathVariable String category) throws JsonProcessingException {
        List<Teacher> response = forumService.getTeachersByCategory(category);
        if(response.isEmpty()){
            throw new CategoryMembersNotFoundException(category);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/prowadzacy/ranking")
    @Operation(summary = "Returns teachers who belong to the specified category ranked by their average rating.")
    public ResponseEntity<List<Teacher>> getTeachersRankedByCategory(@RequestParam("kategoria") String category) throws JsonProcessingException {
        List<Teacher> response = forumService.getBestTeachersRankedByCategory(category);
        if(response.isEmpty()){
            throw new CategoryMembersNotFoundException(category);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/prowadzacy/{category}/ranking/najlepsi")
    @Operation(summary = "Returns limited number of best rated teachers who belong to the specified category, example reviews are provided.",
        description = "Number of return teachers is specified by the limit parameter, each teacher has a maximal example of three associated reviews.")
    public ResponseEntity<List<Teacher>> getBestRankedTeachersByCategoryWithReviewsLimited(@PathVariable String category, @RequestParam("limit") @Min(-1) int limit) throws JsonProcessingException {

        List<Teacher> response = forumService.getBestRankedTeachersByCategoryLimited(category, limit);
        if(response.isEmpty()){
            throw new CategoryMembersNotFoundException(category);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/prowadzacy/{category}/ranking/najgorsi")
    @Operation(summary = "Returns limited number of worst rated teachers who belong to the specified category, example reviews are provided.",
            description = "Number of return teachers is specified by the limit parameter, each teacher has a maximal example of three associated reviews.")
    public ResponseEntity<List<Teacher>> getWorstRankedTeachersByCategoryWithReviewsLimited(@PathVariable String category, @RequestParam("limit") @Min(-1) int limit) throws JsonProcessingException {
        List<Teacher> response = forumService.getWorstRankedTeachersByCategoryLimited(category, limit);
        if(response.isEmpty()){
            throw new CategoryMembersNotFoundException(category);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
