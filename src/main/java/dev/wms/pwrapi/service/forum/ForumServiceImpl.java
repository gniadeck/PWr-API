package dev.wms.pwrapi.service.forum;

import dev.wms.pwrapi.dao.forum.DatabaseMetadataRepository;
import dev.wms.pwrapi.dao.forum.ReviewRepository;
import dev.wms.pwrapi.dao.forum.TeacherRepository;
import dev.wms.pwrapi.entity.forum.*;

import dev.wms.pwrapi.utils.forum.consts.Category;
import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO;
import dev.wms.pwrapi.utils.forum.dto.ReviewWithTeacherDTO;
import dev.wms.pwrapi.utils.forum.dto.TeacherInfoDTO;
import dev.wms.pwrapi.utils.forum.dto.TeacherWithReviewsDTO;
import dev.wms.pwrapi.utils.forum.exceptions.ReviewNotFoundException;
import dev.wms.pwrapi.utils.forum.exceptions.TeacherNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForumServiceImpl {
    private final DatabaseMetadataRepository databaseMetadataRepository;
    private final ReviewRepository reviewRepository;
    private final TeacherRepository teacherRepository;
    private final int TEACHER_RANKING_SAMPLE_REVIEWS_LIMIT = 3;

    public DatabaseMetadataDTO getDatabaseMetadata() {
        return databaseMetadataRepository.getDatabaseMetadata();
    }

    public DatabaseMetadataDTO getTotalReviews() {
        return DatabaseMetadataDTO.ofReviewCount(reviewRepository.count());
    }

    public DatabaseMetadataDTO getTotalTeachers() {
        return DatabaseMetadataDTO.ofTeacherCount(teacherRepository.count());
    }

    public ReviewWithTeacherDTO getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(review -> ReviewWithTeacherDTO.of(review,
                        teacherRepository.findById(review.getTeacherId()).get()
                ))
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
    }

    public TeacherWithReviewsDTO getTeacherWithAllReviewsById(Long teacherId) {
        return teacherRepository.findById(teacherId)
                .map(teacher -> mapToTeacherWithReviews(teacher, Pageable.unpaged()))
                .orElseThrow(() -> new TeacherNotFoundException(teacherId));
    }

    public TeacherWithReviewsDTO getTeacherWithLimitedReviewsById(Long teacherId, int limit) {
        return limit == -1
                ? getTeacherWithAllReviewsById(teacherId)
                : teacherRepository.findById(teacherId)
                .map(teacher -> mapToTeacherWithReviews(teacher, Pageable.ofSize(limit)))
                .orElseThrow(() -> new TeacherNotFoundException(teacherId));
    }

    public TeacherWithReviewsDTO getTeacherWithLimitedReviewsByFullName(String firstName, String lastName, String query,
                                                                        int limit) {
        Optional<Teacher> teacherOptional = query != null ? teacherRepository.findTeacherByFullNameLikeIgnoreCase(query.trim()) :
                teacherRepository.findTeacherByFullNameLikeIgnoreCaseOrFullNameLikeIgnoreCase(firstName + " " + lastName,
                        lastName + " " + firstName);
        return teacherOptional
                .map(teacher -> mapToTeacherWithReviews(teacher, Pageable.ofSize(limit)))
                .orElse(new TeacherWithReviewsDTO());
    }

    public Optional<Teacher> findFirstByFullNameContaining(String query) {
        return teacherRepository.findByFullNameContainingIgnoreCase(query)
                .filter(result -> !result.isEmpty())
                .map(result -> result.get(0));
    }

    public Set<TeacherInfoDTO> getTeachersInfoByCategory(Category category) {
        return mapToTeacherInfoDTOs(teacherRepository.getTeachersByCategory(category));
    }

    public Set<TeacherInfoDTO> getBestTeachersOfCategory(Category category) {
        return mapToTeacherInfoDTOs(teacherRepository.getTeachersByCategoryOrderByAverageRatingDesc(category));
    }

    private Set<TeacherInfoDTO> mapToTeacherInfoDTOs(List<Teacher> teachers) {
        return teachers
                .stream()
                .map(TeacherInfoDTO::fromTeacher)
                .collect(Collectors.toSet());
    }

    public Set<TeacherWithReviewsDTO> getBestTeachersInfoByCategoryLimitedWithExampleReviews(Category category, int limit) {
        return teacherRepository.getTeachersByCategoryOrderByAverageRatingDesc(category, Pageable.ofSize(limit))
                .stream()
                .map(teacher -> mapToTeacherWithReviews(teacher, Pageable.ofSize(TEACHER_RANKING_SAMPLE_REVIEWS_LIMIT)))
                .collect(Collectors.toSet());
    }

    public Set<TeacherWithReviewsDTO> getWorstTeachersInfoByCategoryLimitedWithExampleReviews(Category category, int limit) {
        return teacherRepository.getTeachersByCategoryOrderByAverageRatingAsc(category, Pageable.ofSize(limit))
                .stream()
                .map(teacher -> mapToTeacherWithReviews(teacher, Pageable.ofSize(TEACHER_RANKING_SAMPLE_REVIEWS_LIMIT)))
                .collect(Collectors.toSet());
    }

    private TeacherWithReviewsDTO mapToTeacherWithReviews(Teacher teacher, Pageable pageable) {
        return TeacherWithReviewsDTO.of(teacher, reviewRepository.getReviewsByTeacherId(teacher.getTeacherId(), pageable));
    }
}
