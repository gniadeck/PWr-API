package dev.wms.pwrapi.model.studentStats;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StudentStatsData {
    private StudentStatsPersonalData personalData;
    private List<StudentStatsObject> content;
}
