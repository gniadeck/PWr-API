package dev.wms.pwrapi.utils.forum.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.wms.pwrapi.entity.forum.Review;
import dev.wms.pwrapi.entity.forum.Teacher;
import dev.wms.pwrapi.utils.forum.consts.Category;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public class TeacherWithReviewsDTO {
    private Long id;
    private Category category;
    private String academicTitle;
    private String fullName;
    private BigDecimal average;
    private List<Review> reviews;

    public static TeacherWithReviewsDTO of(Teacher teacher, List<Review> reviews){
        return TeacherWithReviewsDTO.builder()
                .id(teacher.getTeacherId())
                .category(teacher.getCategory())
                .academicTitle(teacher.getAcademicTitle())
                .fullName(teacher.getFullName())
                .average(teacher.getAverageRating())
                .reviews(reviews)
                .build();
    }
}


