package dev.wms.pwrapi.entity.forum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class Review_r {
    @Id
    private Long reviewId;
    private String courseName;
    private Double givenRating;
    private String title;
    private String review;
    private String reviewer;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postDate;
    private Teacher_r teacher;
}
