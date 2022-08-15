package dev.wms.pwrapi.entity.jsos.finance.operations;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FinanceOperationResult {

    private String accountNumber;
    private String saldo;
    private String incomeSum;
    private String outcomeSum;
    private String unpaidAmount;
    private List<OperationEntry> entries;

}
