package dev.wms.pwrapi.dto.isjsosdown;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
public record HeadToHeadDTO(List<AdditionalStatsDTO> servicesStats) {
}
