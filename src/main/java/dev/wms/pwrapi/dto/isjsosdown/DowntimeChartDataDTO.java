package dev.wms.pwrapi.dto.isjsosdown;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
public record DowntimeChartDataDTO(LocalDate dateOfDowntimes, int numberOfDowntimes, Long totalDowntimeLengthMillis) {
}
