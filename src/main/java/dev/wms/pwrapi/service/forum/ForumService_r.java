package dev.wms.pwrapi.service.forum;

import dev.wms.pwrapi.entity.forum.*;
import dev.wms.pwrapi.repository.DatabaseMetadataRepository;
import dev.wms.pwrapi.repository.ReviewRepository;
import dev.wms.pwrapi.repository.TeacherRepository;

import static dev.wms.pwrapi.utils.forum.consts.Category.*;

import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO_r;
import dev.wms.pwrapi.utils.forum.exceptions.CategoryMembersNotFoundException;
import dev.wms.pwrapi.utils.forum.exceptions.ReviewNotFoundException;
import dev.wms.pwrapi.utils.forum.exceptions.TeacherNotFoundByFullNameException;
import dev.wms.pwrapi.utils.forum.exceptions.TeacherNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ForumService_r {

    private final DatabaseMetadataRepository databaseMetadataRepository;
    private final ReviewRepository reviewRepository;
    private final TeacherRepository teacherRepository;
    private final Long WORST_AND_BEST_TEACHERS_REVIEW_LIMIT = 3L;
    private final Set<String> categories =
            Set.of(
                    MATEMATYCY.name(), FIZYCY.name(), INFORMATYCY.name(), CHEMICY.name(), ELEKTRONICY.name(),
                    JEZYKOWCY.name(), SPORTOWCY.name(), HUMANISCI.name(), INNI.name()
            );

    public DatabaseMetadataDTO_r getDatabaseMetadata() {
        return databaseMetadataRepository.getDatabaseMetadata();
    }

    public DatabaseMetadataDTO_r getTotalReviews() {
        return DatabaseMetadataDTO_r.builder()
                .totalReviews(reviewRepository.getTotalNumberOfReviews())
                .build();
    }

    public Review_r getReviewById(Long reviewId) {
        Optional<Review_r> review = reviewRepository.getReviewWithoutTeacherById(reviewId);
        review.ifPresentOrElse(
                r -> r.setTeacher(reviewRepository.getReviewRecipient(reviewId)),
                () -> {
                    throw new ReviewNotFoundException(reviewId);
                });
        return review.get();
    }

    public DatabaseMetadataDTO_r getTotalTeachers() {
        return DatabaseMetadataDTO_r.builder()
                .totalTeachers(teacherRepository.getTotalNumberOfTeachers())
                .build();
    }

    public TeacherWithReviewsDTO getTeacherWithAllReviewsById(Long teacherId){
        return teacherRepository.getTeacherInfo(teacherId)
                .map(t -> {
                    return TeacherWithReviewsDTO.builder()
                            .id(t.getId())
                            .category(t.getCategory())
                            .academicTitle(t.getAcademicTitle())
                            .fullName(t.getFullName())
                            .average(t.getAverage())
                            .reviews(reviewRepository.getTeacherReviews(t.getId()))
                            .build();
                })
                .orElseThrow(() -> new TeacherNotFoundException(teacherId));
    }

    public TeacherWithReviewsDTO getTeacherWithLimitedReviewsById(Long teacherId, Long limit){
        return limit == - 1
                ? getTeacherWithAllReviewsById(teacherId)
                : teacherRepository.getTeacherInfo(teacherId)
                    .map(t -> {
                        return TeacherWithReviewsDTO.builder()
                                .id(t.getId())
                                .category(t.getCategory())
                                .academicTitle(t.getAcademicTitle())
                                .fullName(t.getFullName())
                                .average(t.getAverage())
                                .reviews(reviewRepository.getTeacherReviewsLimited(teacherId, limit))
                                .build();
                    })
                    .orElseThrow(() -> new TeacherNotFoundException(teacherId));
    }

    public TeacherWithReviewsDTO getTeacherWithLimitedReviewsByFullName(String firstName, String lastName, Long limit){
        Long teacherId = getTeacherIdByFullName(firstName, lastName);
        return getTeacherWithLimitedReviewsById(teacherId, limit);
    }

    // TODO -> work on LIKE
    private Long getTeacherIdByFullName(String firstName, String lastName){
        Optional<Long> teacherId = teacherRepository.getTeacherIdByFullName("%" + firstName + "%", "%" + lastName + "%");
        if(teacherId.isEmpty()){
            throw new TeacherNotFoundByFullNameException(firstName, lastName);
        }
        return teacherId.get();
    }

    public Set<TeacherInfoDTO> getTeachersInfoByCategory(String category){
        checkIfCategoryExists(category.toUpperCase());
        return teacherRepository.getTeachersInfoByCategory(category);
    }

    public Set<TeacherInfoDTO> getBestTeachersOfCategory(String category){
        checkIfCategoryExists(category.toUpperCase());
        return teacherRepository.getBestTeachersOfCategory(category);
    }

    private void checkIfCategoryExists(String category){
        if(!categories.contains(category.toUpperCase())){
            throw new CategoryMembersNotFoundException(category);
        }
    }

    public Set<TeacherWithReviewsDTO> getLimitedBestTeachersOfCategoryWithExampleReviews(String category, int limit){
        checkIfCategoryExists(category);
        return getBestTeachersInfoByCategoryLimited(category, limit).stream()
                    .map(teacherInfo -> TeacherWithReviewsDTO.builder()
                            .id(teacherInfo.getId())
                            .category(teacherInfo.getCategory())
                            .academicTitle(teacherInfo.getAcademicTitle())
                            .fullName(teacherInfo.getFullName())
                            .average(teacherInfo.getAverage())
                            .reviews(reviewRepository.getTeacherReviewsLimited(teacherInfo.getId(),
                                    WORST_AND_BEST_TEACHERS_REVIEW_LIMIT))
                            .build()
                    )
                    .collect(Collectors.toSet());
    }

    private Set<TeacherInfoDTO> getBestTeachersInfoByCategoryLimited(String category, int limit){
        if(limit == -1){
            return teacherRepository.getBestTeachersOfCategory(category);
        }
        return teacherRepository.getBestTeachersOfCategoryLimited(category, limit);
    }

    public Set<TeacherWithReviewsDTO> getLimitedWorstTeachersOfCategoryWithExampleReviews(String category, Long limit){
        checkIfCategoryExists(category);
        return getWorstTeachersInfoByCategoryLimited(category, limit).stream()
                .map(teacherInfo -> TeacherWithReviewsDTO.builder()
                        .id(teacherInfo.getId())
                        .category(teacherInfo.getCategory())
                        .academicTitle(teacherInfo.getAcademicTitle())
                        .fullName(teacherInfo.getFullName())
                        .average(teacherInfo.getAverage())
                        .reviews(reviewRepository.getTeacherReviewsLimited(teacherInfo.getId(),
                                WORST_AND_BEST_TEACHERS_REVIEW_LIMIT))
                        .build()
                )
                .collect(Collectors.toSet());
    }

    private Set<TeacherInfoDTO> getWorstTeachersInfoByCategoryLimited(String category, Long limit){
        if(limit == -1){
            return teacherRepository.getWorstTeachersOfCategory(category);
        }
        return teacherRepository.getWorstTeachersOfCategoryLimited(category, limit);
    }
}
