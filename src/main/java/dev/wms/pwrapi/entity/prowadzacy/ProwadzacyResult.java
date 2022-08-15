package dev.wms.pwrapi.entity.prowadzacy;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProwadzacyResult {

    private String title;
    private ProwadzacyDay pn;
    private ProwadzacyDay wt;
    private ProwadzacyDay sr;
    private ProwadzacyDay czw;
    private ProwadzacyDay pt;
    private ProwadzacyDay sb;
    private ProwadzacyDay nd;
    private String icalLink;


}
