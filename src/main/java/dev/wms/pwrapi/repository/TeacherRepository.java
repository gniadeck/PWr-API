package dev.wms.pwrapi.repository;

import dev.wms.pwrapi.entity.forum.TeacherInfoDTO;
import dev.wms.pwrapi.entity.forum.Teacher_r;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeacherRepository extends PagingAndSortingRepository<Teacher_r, Long> {
    @Query("SELECT COUNT(*) FROM teacher")
    Long getTotalNumberOfTeachers();

    @Query("SELECT teacher_id, category, academic_title, full_name, average_rating " +
            "FROM teacher t " +
            "WHERE t.teacher_id = :teacherId")
    TeacherInfoDTO getTeacherInfo(@Param("teacherId") Long teacherId);
}