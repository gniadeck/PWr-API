package dev.wms.pwrapi.entity.forum;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Builder
@Data
public class TeacherWithReviewsDTO {
    @Id
    private Long teacherId;
    private String category;
    private String academicTitle;
    private String fullName;
    private Double averageRating;
    private Set<Review_r> reviews;
}
