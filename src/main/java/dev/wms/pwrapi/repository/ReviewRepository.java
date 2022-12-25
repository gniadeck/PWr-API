package dev.wms.pwrapi.repository;

import dev.wms.pwrapi.entity.forum.Review_r;
import dev.wms.pwrapi.entity.forum.Teacher_r;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReviewRepository extends PagingAndSortingRepository<Review_r, Long> {

    @Query("SELECT COUNT(*) FROM review")
    Long getTotalNumberOfReviews();

    @Query("SELECT review_id AS 'reviewId', course_name AS 'courseName', given_rating AS 'givenRating', title AS 'title'," +
            " review, reviewer, post_date AS 'postDate' " +
            "FROM review r " +
            "WHERE r.review_id = :reviewId")
    Optional<Review_r> getReviewWithoutTeacherById(@Param("reviewId") Long reviewId);

    @Query("SELECT * " +
            "FROM teacher t " +
            "WHERE t.teacher_id = (SELECT teacher_id FROM review r WHERE r.review_id = :reviewId)")
    Teacher_r getReviewRecipent(@Param("reviewId") Long reviewId);

    @Query("SELECT review_id, course_name, given_rating, title, review, reviewer, post_date " +
            "FROM review r " +
            "WHERE r.teacher_id = :teacherId")
    Set<Review_r> getTeacherReviews(@Param("teacherId") Long teacherId);

    @Query("SELECT review_id, course_name, given_rating, title, review, reviewer, post_date " +
            "FROM review r " +
            "WHERE r.teacher_id = :teacherId " +
            "LIMIT :limit")
    Set<Review_r> getTeacherReviewsLimited(@Param("teacherId") Long teacherId, @Param("limit") Long limit);
}