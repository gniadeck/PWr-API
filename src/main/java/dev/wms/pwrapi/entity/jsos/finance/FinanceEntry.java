package dev.wms.pwrapi.entity.jsos.finance;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinanceEntry {

    private String dataNaliczenia;
    private String amount;
    private String name;
    private String rata;
    private String terminPlatnosci;
    private String status;

}
