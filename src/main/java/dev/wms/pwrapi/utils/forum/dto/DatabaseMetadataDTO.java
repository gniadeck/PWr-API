package dev.wms.pwrapi.utils.forum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Value
@Builder
public class DatabaseMetadataDTO {
    Long totalTeachers;
    Long totalReviews;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime latestRefresh;

    public static DatabaseMetadataDTO ofTeacherCount(Long teacherCount){
        return DatabaseMetadataDTO.builder()
                .totalTeachers(teacherCount)
                .build();
    }

    public static DatabaseMetadataDTO ofReviewCount(Long reviewCount){
        return DatabaseMetadataDTO.builder()
                .totalReviews(reviewCount)
                .build();
    }
}