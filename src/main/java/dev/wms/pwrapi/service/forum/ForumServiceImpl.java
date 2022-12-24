package dev.wms.pwrapi.service.forum;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.dao.forum.ForumDAO;
import dev.wms.pwrapi.dao.forum.ForumDAOImpl;
import dev.wms.pwrapi.entity.forum.Review;
import dev.wms.pwrapi.entity.forum.Teacher;
import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ForumServiceImpl implements ForumService {

    private final ForumDAO forumDAO;

    @Autowired
    public ForumServiceImpl(ForumDAOImpl forumDAOImpl){
        this.forumDAO = forumDAOImpl;
    }


    @Override
    public DatabaseMetadataDTO getDatabaseMetadata() {
        int totalTeachers = forumDAO.getNumberOfTeachers();
        int totalReviews = forumDAO.getNumberOfReviews();
        String latestRefreshDate = forumDAO.getLastRefreshDate();
        DatabaseMetadataDTO databaseMetadataDTO = new DatabaseMetadataDTO(totalTeachers, totalReviews, latestRefreshDate);
        return databaseMetadataDTO;
    }

    @Override
    public DatabaseMetadataDTO getTotalReviews() {
        int totalReviews = forumDAO.getNumberOfReviews();
        DatabaseMetadataDTO databaseMetadataDTO = new DatabaseMetadataDTO();
        databaseMetadataDTO.setTotalReviews(totalReviews);
        return databaseMetadataDTO;
    }

    @Override
    public Review getReviewById(int id) {
        Review review = forumDAO.findReviewById(id);
        return review;
    }

    @Override
    public DatabaseMetadataDTO getTotalTeachers() {
        int totalTeachers = forumDAO.getNumberOfTeachers();
        DatabaseMetadataDTO databaseMetadataDTO = new DatabaseMetadataDTO();
        databaseMetadataDTO.setTotalTeachers(totalTeachers);
        return databaseMetadataDTO;
    }

    @Override
    public Teacher fetchLimitedTeacherReviewsById(int teacherId, int limit) {
        if(limit == -1) {
            Teacher teacher = forumDAO.fetchTeacherByIdWithReviews(teacherId);
            return teacher;
        }
        Teacher teacher = forumDAO.fetchTeacherByIdWithLimitedReviews(teacherId, limit);
        return teacher;
    }

    @Override
    public Teacher fetchLimitedTeacherReviewsByFullName(String firstName, String lastName, int limit) {
        if(limit == -1){
            Teacher teacher = forumDAO.fetchTeacherByFullNameWithReviews(firstName, lastName);
            return teacher;
        }
        Teacher teacher = forumDAO.fetchTeacherByFullNameWithLimitedReviews(firstName, lastName, limit);
        return teacher;
    }

    @Override
    public List<Teacher> getTeachersByCategory(String category)  {
        return forumDAO.getTeachersByCategory(category);
    }

    @Override
    public List<Teacher> getBestTeachersRankedByCategory(String category) {
        return forumDAO.getTeachersRankedByCategory(category, false);
    }

    @Override
    public List<Teacher> getWorstTeachersRankedByCategory(String category) {
        List<Teacher> teachers = forumDAO.getTeachersRankedByCategory(category, false);
        return teachers;
    }

    @Override
    public List<Teacher> getBestRankedTeachersByCategoryLimited(String category, int limit) {
        return forumDAO.fetchWorstOrBestTeachersByCategoryWithReviewsLimited(category, limit, 3, true);
    }

    @Override
    public List<Teacher> getWorstRankedTeachersByCategoryLimited(String category, int limit) {
        return forumDAO.fetchWorstOrBestTeachersByCategoryWithReviewsLimited(category, limit, 3, false);
    }
}
