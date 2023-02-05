package dev.wms.pwrapi.entity.forum;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Teacher_r {
    @Id
    private Long teacherId;
    private String category;
    private String academicTitle;
    private String fullName;
    private float averageRating;
}
