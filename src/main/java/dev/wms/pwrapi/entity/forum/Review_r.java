package dev.wms.pwrapi.entity.forum;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Review_r {
    @Id
    private Long reviewId;
    private String courseName;
    private Double givenRating;
    private String title;
    private String review;
    private String reviewer;
    private LocalDateTime postDate;
    private Teacher_r teacher;
}
