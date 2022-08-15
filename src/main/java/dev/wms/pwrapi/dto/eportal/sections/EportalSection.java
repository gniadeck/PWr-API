package dev.wms.pwrapi.dto.eportal.sections;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EportalSection {

    private String sectionName;
    private ArrayList<EportalSectionElement> elements;
    
}
