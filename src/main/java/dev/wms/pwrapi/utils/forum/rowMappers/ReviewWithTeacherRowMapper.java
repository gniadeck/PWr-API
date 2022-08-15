package dev.wms.pwrapi.utils.forum.rowMappers;

import dev.wms.pwrapi.entity.forum.Review;
import dev.wms.pwrapi.entity.forum.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewWithTeacherRowMapper implements RowMapper<Review> {

    private TeacherRowMapper teacherRowMapper;
    private ReviewRowMapper reviewRowMapper;

    @Autowired
    public ReviewWithTeacherRowMapper(TeacherRowMapper teacherRowMapper, ReviewRowMapper reviewRowMapper){
        this.teacherRowMapper = teacherRowMapper;
        this.reviewRowMapper = reviewRowMapper;
    }

    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Teacher teacher = teacherRowMapper.mapRow(rs, rowNum);
        Review review = reviewRowMapper.mapRow(rs, rowNum);

        review.setTeacher(teacher);
        return review;
    }
}
