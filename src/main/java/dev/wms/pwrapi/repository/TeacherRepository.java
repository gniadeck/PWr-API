package dev.wms.pwrapi.repository;

import dev.wms.pwrapi.entity.forum.Teacher_r;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TeacherRepository extends PagingAndSortingRepository<Teacher_r, Long> {
    @Query("SELECT COUNT(*) FROM teacher")
    Long getTotalNumberOfTeachers();
}