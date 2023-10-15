package dev.wms.pwrapi.utils.forum.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import dev.wms.pwrapi.entity.forum.Review;
import dev.wms.pwrapi.entity.forum.Teacher;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class ReviewWithTeacherDTO {
    Long id;
    String courseName;
    BigDecimal givenRating;
    String title;
    String review;
    String reviewer;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime postDate;
    Teacher teacher;

    public static ReviewWithTeacherDTO of(Review review, Teacher teacher){
        return ReviewWithTeacherDTO.builder()
                .id(review.getReviewId())
                .courseName(review.getCourseName())
                .givenRating(review.getGivenRating())
                .title(review.getTitle())
                .review(review.getReview())
                .reviewer(review.getReviewer())
                .postDate(review.getPostDate())
                .teacher(teacher)
                .build();
    }
}
