package dev.wms.pwrapi.service.forum;

import dev.wms.pwrapi.entity.forum.*;
import dev.wms.pwrapi.repository.DatabaseMetadataRepository;
import dev.wms.pwrapi.repository.ReviewRepository;
import dev.wms.pwrapi.repository.TeacherRepository;
import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO_r;
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

    public DatabaseMetadataDTO_r getDatabaseMetadata() {
        return databaseMetadataRepository.getDatabaseMetadata();
    }

    public DatabaseMetadataDTO_r getTotalReviews() {
        DatabaseMetadataDTO_r databaseMetadataDTO = new DatabaseMetadataDTO_r();
        databaseMetadataDTO.setTotalReviews(reviewRepository.getTotalNumberOfReviews());
        return databaseMetadataDTO;
    }

    public Optional<Review_r> getReviewById(Long reviewId) {
        Optional<Review_r> review = reviewRepository.getReviewWithoutTeacherById(reviewId);
        review.ifPresent(r -> r.setTeacher(reviewRepository.getReviewRecipent(reviewId)));
        return review;
    }

    public DatabaseMetadataDTO_r getTotalTeachers() {
        DatabaseMetadataDTO_r databaseMetadataDTO = new DatabaseMetadataDTO_r();
        databaseMetadataDTO.setTotalTeachers(teacherRepository.getTotalNumberOfTeachers());
        return databaseMetadataDTO;
    }

    // TODO -> fix
    public TeacherWithReviewsDTO getTeacherWithAllReviewsById(Long teacherId){
        //checkIfTeacherExists(teacherId);
        TeacherWithReviewsDTO teacherInfo = getTeacherInfo(teacherId);
        teacherInfo.setReviews(reviewRepository.getTeacherReviews(teacherId));
        return teacherInfo;
    }

    public TeacherWithReviewsDTO getTeacherWithLimitedReviewsById(Long teacherId, Long limit){
        if(limit == -1){
            return getTeacherWithAllReviewsById(teacherId);
        }
//        checkIfTeacherExistsById(teacherId);
        TeacherWithReviewsDTO teacherInfo = getTeacherInfo(teacherId);
        teacherInfo.setReviews(reviewRepository.getTeacherReviewsLimited(teacherId, limit));
        return teacherInfo;
    }

    public TeacherWithReviewsDTO getTeacherWithLimitedReviewsByFullName(String firstName, String lastName, Long limit){
        Long teacherId = getTeacherIdByFullName(firstName, lastName);
        return getTeacherWithLimitedReviewsById(teacherId, limit);
    }

    private TeacherWithReviewsDTO getTeacherInfo(Long teacherId){
        TeacherInfoDTO teacherInfo = teacherRepository.getTeacherInfo(teacherId);
        return TeacherWithReviewsDTO.builder()
                .teacherId(teacherInfo.getTeacherId())
                .category(teacherInfo.getCategory())
                .academicTitle(teacherInfo.getAcademicTitle())
                .fullName(teacherInfo.getFullName())
                .averageRating(teacherInfo.getAverageRating())
                .build();
    }

    public Set<TeacherInfoDTO> getTeachersInfoByCategory(String category){
        return teacherRepository.getTeachersInfoByCategory(category);
    }

    public Set<TeacherInfoDTO> getBestTeachersOfCategory(String category){
        return teacherRepository.getBestTeachersOfCategory(category);
    }

    public Set<TeacherWithReviewsDTO> getLimitedBestTeachersOfCategoryWithExampleReviews(String category, Long limit){
        return teacherRepository.getBestTeachersOfCategoryLimited(category, limit).stream()
                    .map(teacherInfo -> TeacherWithReviewsDTO.builder()
                            .teacherId(teacherInfo.getTeacherId())
                            .category(teacherInfo.getCategory())
                            .academicTitle(teacherInfo.getAcademicTitle())
                            .fullName(teacherInfo.getFullName())
                            .averageRating(teacherInfo.getAverageRating())
                            .reviews(reviewRepository.getTeacherReviewsLimited(teacherInfo.getTeacherId(),
                                    WORST_AND_BEST_TEACHERS_REVIEW_LIMIT))
                            .build()
                    )
                    .collect(Collectors.toSet());
    }

    public Set<TeacherWithReviewsDTO> getLimitedWorstTeachersOfCategoryWithExampleReviews(String category, Long limit){
        Long reviewLimit = 3L;
        return teacherRepository.getWorstTeachersOfCategoryLimited(category, limit).stream()
                .map(teacherInfo -> TeacherWithReviewsDTO.builder()
                        .teacherId(teacherInfo.getTeacherId())
                        .category(teacherInfo.getCategory())
                        .academicTitle(teacherInfo.getAcademicTitle())
                        .fullName(teacherInfo.getFullName())
                        .averageRating(teacherInfo.getAverageRating())
                        .reviews(reviewRepository.getTeacherReviewsLimited(teacherInfo.getTeacherId(),
                                WORST_AND_BEST_TEACHERS_REVIEW_LIMIT))
                        .build()
                )
                .collect(Collectors.toSet());
    }

    private void checkIfTeacherExistsById(Long teacherId){
        if(!teacherRepository.existsById(teacherId)){
            throw new TeacherNotFoundException(teacherId);
        }
    }
    private Long getTeacherIdByFullName(String firstName, String lastName){
        /*
        Long teacherId = teacherRepository.getTeacherIdByFullName(firstName, lastName);
        if(teacherId == null){
            throw new RuntimeException("teacher not present!");
        }
        return teacherId;
        */
        return teacherRepository.getTeacherIdByFullName(firstName, lastName);
    }
}
