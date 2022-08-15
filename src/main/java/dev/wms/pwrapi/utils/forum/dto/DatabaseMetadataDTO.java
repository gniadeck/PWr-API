package dev.wms.pwrapi.utils.forum.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseMetadataDTO {

    private Integer totalTeachers;
    private Integer totalReviews;
    private String latestRefresh;

}
