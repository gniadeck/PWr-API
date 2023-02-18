package dev.wms.pwrapi.entity.forum;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Teacher {
    @Id
    private Long id;
    private String category;
    private String academicTitle;
    private String fullName;
    private float average;
}
