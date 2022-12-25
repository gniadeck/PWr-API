package dev.wms.pwrapi.service.forum;

import dev.wms.pwrapi.entity.forum.*;
import dev.wms.pwrapi.repository.DatabaseMetadataRepository;
import dev.wms.pwrapi.repository.ReviewRepository;
import dev.wms.pwrapi.repository.TeacherRepository;
import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO_r;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ForumService_r {

    private final DatabaseMetadataRepository databaseMetadataRepository;
    private final ReviewRepository reviewRepository;
    private final TeacherRepository teacherRepository;

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
    public TeacherWithReviewsDTO getTeacherWithAllReviews(Long teacherId){
        //checkIfTeacherExists(teacherId);
        TeacherInfoDTO teacherInfo = teacherRepository.getTeacherInfo(teacherId);
        return TeacherWithReviewsDTO.builder()
                .teacherId(teacherInfo.getTeacherId())
                .category(teacherInfo.getCategory())
                .academicTitle(teacherInfo.getAcademicTitle())
                .fullName(teacherInfo.getFullName())
                .averageRating(teacherInfo.getAverageRating())
                .reviews(reviewRepository.getTeacherReviews(teacherId))
                .build();
    }

    private void checkIfTeacherExists(Long teacherId){
        if(!teacherRepository.existsById(teacherId)){
            /*
            // TODO -> 404 status code
            throw new RuntimeException("teacher does not exist :)");
            */
        }
    }

    public TeacherWithReviewsDTO getTeacherWithLimitedReviews(Long teacherId, Long limit){
//        checkIfTeacherExists(teacherId);
        if(limit == -1){
            return getTeacherWithAllReviews(teacherId);
        }
        TeacherInfoDTO teacherInfo = teacherRepository.getTeacherInfo(teacherId);
        return TeacherWithReviewsDTO.builder()
                .teacherId(teacherInfo.getTeacherId())
                .category(teacherInfo.getCategory())
                .academicTitle(teacherInfo.getAcademicTitle())
                .fullName(teacherInfo.getFullName())
                .averageRating(teacherInfo.getAverageRating())
                .reviews(reviewRepository.getTeacherReviewsLimited(teacherId, limit))
                .build();
    }

}
