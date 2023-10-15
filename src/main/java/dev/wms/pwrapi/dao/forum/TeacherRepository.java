package dev.wms.pwrapi.dao.forum;

import dev.wms.pwrapi.entity.forum.Teacher;
import dev.wms.pwrapi.utils.forum.consts.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends PagingAndSortingRepository<Teacher, Long> {
    Optional<Teacher> findTeacherByFullNameLikeIgnoreCase(String query);
    Optional<Teacher> findTeacherByFullNameLikeIgnoreCaseOrFullNameLikeIgnoreCase(String firstVariant, String secondVariant);
    List<Teacher> getTeachersByCategory(Category category);
    List<Teacher> getTeachersByCategoryOrderByAverageRatingDesc(Category category);
    List<Teacher> getTeachersByCategoryOrderByAverageRatingDesc(Category category, Pageable pageable);
    List<Teacher> getTeachersByCategoryOrderByAverageRatingAsc(Category category, Pageable pageable);
    Optional<List<Teacher>> findByFullNameContainingIgnoreCase(String fullName);
}