package dev.wms.pwrapi.utils.forum.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.wms.pwrapi.entity.forum.Review;
import dev.wms.pwrapi.entity.forum.Teacher;
import dev.wms.pwrapi.utils.forum.consts.Category;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public class TeacherInfoDTO {
    private Long id;
    private Category category;
    private String academicTitle;
    private String fullName;
    private BigDecimal average;

    public static TeacherInfoDTO fromTeacher(Teacher teacher){
        return TeacherInfoDTO.builder()
                .id(teacher.getTeacherId())
                .category(teacher.getCategory())
                .academicTitle(teacher.getAcademicTitle())
                .fullName(teacher.getFullName())
                .average(teacher.getAverageRating())
                .build();
    }

    public TeacherWithReviewsDTO toTeacherWithReviewsDTO(List<Review> reviews){
        return TeacherWithReviewsDTO.builder()
                .id(id)
                .category(category)
                .academicTitle(academicTitle)
                .fullName(fullName)
                .average(average)
                .reviews(reviews)
                .build();
    }
}
