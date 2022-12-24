package dev.wms.pwrapi.entity.forum;

import lombok.Data;

@Data
public class TeacherInfoDTO {
    private Long teacherId;
    private String category;
    private String academicTitle;
    private String fullName;
    private Double averageRating;
}
