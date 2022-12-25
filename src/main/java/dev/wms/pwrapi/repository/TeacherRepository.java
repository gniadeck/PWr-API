package dev.wms.pwrapi.repository;

import dev.wms.pwrapi.entity.forum.TeacherInfoDTO;
import dev.wms.pwrapi.entity.forum.Teacher_r;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface TeacherRepository extends PagingAndSortingRepository<Teacher_r, Long> {
    @Query("SELECT COUNT(*) FROM teacher")
    Long getTotalNumberOfTeachers();

    @Query("SELECT teacher_id, category, academic_title, full_name, average_rating " +
            "FROM teacher t " +
            "WHERE t.teacher_id = :teacherId")
    TeacherInfoDTO getTeacherInfo(@Param("teacherId") Long teacherId);

    @Query("SELECT teacher_id, category, academic_title, full_name, average_rating " +
            "FROM teacher t " +
            "WHERE t.category = :category")
    Set<TeacherInfoDTO> getTeachersInfoByCategory(@Param("category") String category);

    @Query("SELECT teacher_id, category, academic_title, full_name, average_rating " +
            "FROM teacher t " +
            "WHERE t.category = :category " +
            "ORDER BY average_rating DESC")
    Set<TeacherInfoDTO> getBestTeachersOfCategory(@Param("category") String category);

    @Query("SELECT teacher_id, category, academic_title, full_name, average_rating " +
            "FROM teacher t " +
            "WHERE t.category = :category " +
            "ORDER BY average_rating DESC " +
            "LIMIT :limit")
    Set<TeacherInfoDTO> getBestTeachersOfCategoryLimited(@Param("category") String category,
                                                                   @Param("limit") Long limit);

    @Query("SELECT teacher_id, category, academic_title, full_name, average_rating " +
            "FROM teacher t " +
            "WHERE t.category = :category " +
            "ORDER BY average_rating ASC " +
            "LIMIT :limit")
    Set<TeacherInfoDTO> getWorstTeachersOfCategoryLimited(@Param("category") String category,
                                                         @Param("limit") Long limit);

    @Query("SELECT teacher_id " +
            "FROM teacher " +
            "WHERE " +
            "full_name LIKE '%':firstName'%':lastName'%' " +
            "OR " +
            "full_name LIKE '%':lastName'%':firstName'%'")
    Long getTeacherIdByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}