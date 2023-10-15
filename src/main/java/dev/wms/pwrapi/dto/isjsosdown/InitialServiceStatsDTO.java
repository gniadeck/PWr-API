package dev.wms.pwrapi.dto.isjsosdown;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InitialServiceStatsDTO {
    private String title;
    private BigDecimal uptime;
    private List<DowntimeDTO> downtimes;
}
