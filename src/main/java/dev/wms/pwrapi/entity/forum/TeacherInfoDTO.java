package dev.wms.pwrapi.entity.forum;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TeacherInfoDTO {
    private Long id;
    private String category;
    private String academicTitle;
    private String fullName;
    private float average;
}
