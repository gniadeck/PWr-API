package dev.wms.pwrapi.entity.jsos.messages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JsosMessageFull {

    private String from;
    private String to;
    private String message;
    private int internalId;

}
