package dev.wms.pwrapi.entity.jsos.finance;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FinanceResult {

    private String sumOfRequiredPayments;
    private List<FinanceEntry> entries;

}
