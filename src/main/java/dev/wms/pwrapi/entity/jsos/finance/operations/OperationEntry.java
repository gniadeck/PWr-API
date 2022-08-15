package dev.wms.pwrapi.entity.jsos.finance.operations;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperationEntry {

    private String operationDate;
    private String value;
    private String title;
    private String paymentGateway;
    private String paymentAccountDetails;

}
