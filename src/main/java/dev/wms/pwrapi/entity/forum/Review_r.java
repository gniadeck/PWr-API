package dev.wms.pwrapi.entity.forum;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Review_r {
    @Id
    private Long reviewId;
    private String courseName;
    private Double givenRating;
    private String title;
    private String review;
    private String reviewer;
    private String postDate;
    private Teacher_r teacher;
}
