package dev.wms.pwrapi.dto.isjsosdown;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InitialDownServiceStatsDTO extends InitialServiceStatsDTO {

    private DateTime downSince;

}
