package dev.wms.pwrapi.entity.forum;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Review {
    @Id
    private Long id;
    private String courseName;
    private Double givenRating;
    private String title;
    private String review;
    private String reviewer;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postDate;
    private Teacher teacher;
}
