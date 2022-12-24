package dev.wms.pwrapi.service.forum;

import dev.wms.pwrapi.entity.forum.Review_r;
import dev.wms.pwrapi.repository.DatabaseMetadataRepository;
import dev.wms.pwrapi.repository.ReviewRepository;
import dev.wms.pwrapi.repository.TeacherRepository;
import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO;
import dev.wms.pwrapi.utils.forum.dto.DatabaseMetadataDTO_r;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
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
}
