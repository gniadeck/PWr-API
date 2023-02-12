package dev.wms.pwrapi.utils.forum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class DatabaseMetadataDTO_r {
    private Long totalTeachers;
    private Long totalReviews;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latestRefresh;
}