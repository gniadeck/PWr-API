package dev.wms.pwrapi.model.studentStats;

import dev.wms.pwrapi.domain.studentstats.StudentStatsCategory;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
public class StudentStatsObject {
    String type;
    StudentStatsCategory category;
    AbstractStudentStatsContent content;

    @Builder
    public StudentStatsObject(StudentStatsCategory category, AbstractStudentStatsContent content){
        this.type = content.getClass().getSimpleName().replaceAll("StudentStats", "")
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toUpperCase();
        this.category = category;
        this.content = content;
    }
}
