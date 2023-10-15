package dev.wms.pwrapi.dto.eportal.sections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EportalSection {

    private String sectionName;
    private List<EportalSectionElement> elements;
    
}
