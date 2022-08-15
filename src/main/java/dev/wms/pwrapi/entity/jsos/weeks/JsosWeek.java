package dev.wms.pwrapi.entity.jsos.weeks;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@ToString
public class JsosWeek {

    private String weekName;
    private JsosDay pn;
    private JsosDay wt;
    private JsosDay sr;
    private JsosDay czw;
    private JsosDay pt;
    private JsosDay sb;
    private JsosDay nd;

    public JsosWeek() {
        this.weekName = "";
        this.pn = new JsosDay();
        this.wt = new JsosDay();
        this.sr = new JsosDay();
        this.czw = new JsosDay();
        this.pt = new JsosDay();
        this.sb = new JsosDay();
        this.nd = new JsosDay();
    }


    
}
