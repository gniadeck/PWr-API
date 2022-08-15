package dev.wms.pwrapi.dto.eportal.sections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EportalSectionElement {



    private String title;
    private String type;


    
}
