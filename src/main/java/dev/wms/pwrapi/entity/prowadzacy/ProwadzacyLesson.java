package dev.wms.pwrapi.entity.prowadzacy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProwadzacyLesson {

    private String time;
    private String title;
    private String location;
    private String teacher;

}
