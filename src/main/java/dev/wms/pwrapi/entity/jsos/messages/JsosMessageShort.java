package dev.wms.pwrapi.entity.jsos.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties("detailsLink")
public class JsosMessageShort {

    private String from;
    private String subject;
    private String date;
    private String priority;
    private boolean read;
    private int internalId;
    private String detailsLink;


}
