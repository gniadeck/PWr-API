package dev.wms.pwrapi.entity.forum;

import dev.wms.pwrapi.utils.forum.consts.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    @Id
    private Long teacherId;
    private Category category;
    private String academicTitle;
    private String fullName;
    private BigDecimal averageRating;
}
