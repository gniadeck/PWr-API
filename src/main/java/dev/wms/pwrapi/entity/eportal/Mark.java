package dev.wms.pwrapi.entity.eportal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mark {

    private String name;
    private String weight;
    private String value;
    private String range;
    private String percentage;
    private String markName;
    private String feedback;
    private String courseParticipation;

}
