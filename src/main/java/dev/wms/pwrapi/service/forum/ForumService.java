package dev.wms.pwrapi.service.forum;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.entity.forum.Review;
import dev.wms.pwrapi.entity.forum.Teacher;
import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO;

import java.util.List;

public interface ForumService {
    DatabaseMetadataDTO getDatabaseMetadata() throws JsonProcessingException;
    DatabaseMetadataDTO getTotalReviews() throws JsonProcessingException;
    Review getReviewById(int id) throws JsonProcessingException;
    DatabaseMetadataDTO getTotalTeachers() throws JsonProcessingException;
    Teacher fetchLimitedTeacherReviewsById(int teacherId, int limit) throws JsonProcessingException;
    Teacher fetchLimitedTeacherReviewsByFullName(String firstName, String lastName, int limit) throws JsonProcessingException;
    List<Teacher> getTeachersByCategory(String category) throws JsonProcessingException;
    List<Teacher> getBestTeachersRankedByCategory(String category) throws JsonProcessingException;

    List<Teacher> getWorstTeachersRankedByCategory(String category) throws JsonProcessingException;

    List<Teacher> getBestRankedTeachersByCategoryLimited(String category, int limit) throws JsonProcessingException;
    List<Teacher> getWorstRankedTeachersByCategoryLimited(String category, int limit) throws JsonProcessingException;
}
