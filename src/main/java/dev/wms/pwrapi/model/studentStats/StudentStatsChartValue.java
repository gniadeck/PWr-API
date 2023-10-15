package dev.wms.pwrapi.model.studentStats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StudentStatsChartValue {
    private Double value;
    private String label;

    public static StudentStatsChartValue of(Double value, String label) {
        return new StudentStatsChartValue(value, label);
    }
}
