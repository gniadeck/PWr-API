package dev.wms.pwrapi.entity.forum;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TeacherWithReviewsDTO {
    @Id
    private Long id;
    private String category;
    private String academicTitle;
    private String fullName;
    private float average;
    private Set<Review> reviews;
}
