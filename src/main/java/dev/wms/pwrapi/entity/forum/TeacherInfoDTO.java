package dev.wms.pwrapi.entity.forum;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherInfoDTO {
    private Long teacherId;
    private String category;
    private String academicTitle;
    private String fullName;
    private Double averageRating;
}
