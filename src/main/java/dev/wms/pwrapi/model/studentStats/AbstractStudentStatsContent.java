package dev.wms.pwrapi.model.studentStats;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@Accessors(chain = true)
public abstract class AbstractStudentStatsContent {
    private String title;
}
