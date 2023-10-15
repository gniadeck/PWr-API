package dev.wms.pwrapi.entity.forum;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Review {
    @Id
    private Long reviewId;
    private String courseName;
    private BigDecimal givenRating;
    private String title;
    private String review;
    private String reviewer;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postDate;
    @JsonIgnore
    private Long teacherId;
}
