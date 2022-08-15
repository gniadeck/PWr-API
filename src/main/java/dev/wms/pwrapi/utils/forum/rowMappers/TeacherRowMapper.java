package dev.wms.pwrapi.utils.forum.rowMappers;

import dev.wms.pwrapi.entity.forum.Teacher;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TeacherRowMapper implements RowMapper<Teacher> {

    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        Teacher teacher = new Teacher();

        teacher.setId(rs.getInt("teacher_id"));
        teacher.setCategory(rs.getString("category"));
        teacher.setFullName(rs.getString("full_name"));
        teacher.setAcademicTitle(rs.getString("academic_title"));
        teacher.setAverage(rs.getDouble("average_rating"));

        return teacher;
    }
}
