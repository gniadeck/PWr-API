package dev.wms.pwrapi.dto.isjsosdown;

import java.math.BigDecimal;

public record DowntimeStatsDTO(String serviceUrl, long recordedDowntimes, BigDecimal totalDowntimesDuration) {
}

