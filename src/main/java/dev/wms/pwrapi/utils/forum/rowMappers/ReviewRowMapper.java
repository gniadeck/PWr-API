package dev.wms.pwrapi.utils.forum.rowMappers;

import dev.wms.pwrapi.entity.forum.Review;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewRowMapper implements RowMapper<Review> {

    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review();

        review.setId(rs.getInt("review_id"));
        review.setCourseName(rs.getString("course_name"));
        review.setGivenRating(rs.getDouble("given_rating"));
        review.setTitle(rs.getString("title"));
        review.setReview(rs.getString("review"));
        review.setReviewer(rs.getString("reviewer"));
        review.setPostDate(rs.getString("post_date"));

        return review;
    }
}
