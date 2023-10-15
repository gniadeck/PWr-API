package dev.wms.pwrapi.dto.isjsosdown;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record AdditionalStatsDTO(String serviceName,
                                 boolean isActive,
                                 long recordedDowntimes,
                                 BigDecimal totalDowntimeMillis,
                                 BigDecimal totalUptimeMillis,
                                 BigDecimal averageDowntimeLengthMillis,
                                 BigDecimal averageUptimeLengthMillis,
                                 BigDecimal recordingStatsSinceMillis,
                                 List<DowntimeChartDataDTO> chart,
                                 TrafficStatsDTO trafficStats) {
}
