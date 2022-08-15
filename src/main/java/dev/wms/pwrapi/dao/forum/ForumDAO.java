package dev.wms.pwrapi.dao.forum;

import dev.wms.pwrapi.entity.forum.Review;
import dev.wms.pwrapi.entity.forum.Teacher;

import java.util.List;

public interface ForumDAO {
    int getNumberOfTeachers();

    int getNumberOfReviews();

    String getLastRefreshDate();

    List<Teacher> fetchAllTeachers();

    List<Teacher> fetchTeachersLimited(int limit);

    List<Review> fetchAllReviews();

    List<Review> fetchReviewsLimited(int limit);

    List<Review> fetchTeacherReviewsById(int teacherId);

    List<Review> fetchTeacherReviewsByFullName(String firstName, String lastName);

    List<Review> fetchTeacherReviewsByIdLimited(int teacherId, int limit);

    List<Review> fetchTeacherReviewsByFullNameLimited(String firstName, String lastName, int limit);

    List<Review> fetchRecentTeacherReviewsByFullNameLimited(String firstName, String lastName, int limit);

    List<Review> fetchOldestTeacherReviewsByFullNameLimited(String firstName, String lastName, int limit);

    List<Review> fetchRecentTeacherReviewsByIdLimited(int teacherId, int limit);

    List<Review> fetchOldestTeacherReviewsByIdLimited(int teacherId, int limit);

    Teacher findTeacherById(int teacherId);

    Teacher findTeacherByName(String firstName, String lastName);

    Review findReviewById(int reviewId);

    List<Teacher> fetchBestRatedTeachersLimited(int limit);

    List<Teacher> fetchWorstOrBestTeachersByCategoryWithReviewsLimited(String category, int teacherLimit, int reviewLimit, boolean isBest);

    List<Teacher> fetchWorstRatedTeachersLimited(int limit);

    Teacher fetchTeacherByIdWithReviews(int teacherId);

    Teacher fetchTeacherByIdWithLimitedReviews(int teacherId, int limit);

    Teacher fetchTeacherByFullNameWithReviews(String firstName, String lastName);

    Teacher fetchTeacherByFullNameWithLimitedReviews(String firstName, String lastName, int limit);

    List<Teacher> getTeachersByCategory(String category);

    List<Teacher> getTeachersRankedByCategory(String category, boolean isAscending);

    List<Teacher> getTeachersRankedByCategoryLimited(String category, int limit, boolean isAscending);

}
