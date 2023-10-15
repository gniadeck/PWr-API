package dev.wms.pwrapi.dto.isjsosdown;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
public record DowntimeStatsRankingDTO(String serviceName, String serviceUrl, long recordedDowntimes,
                                      BigDecimal totalDowntimesDuration, BigDecimal averageDowntimeDuration,
                                      BigDecimal downtimeScore) {

}
