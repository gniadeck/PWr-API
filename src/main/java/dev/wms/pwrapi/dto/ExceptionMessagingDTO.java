package dev.wms.pwrapi.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is used for formatting exceptions that are thrown by our API
 */
@Data
public class ExceptionMessagingDTO {
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String timestamp;
    private String errorMessage;
    private String documentation;

    public ExceptionMessagingDTO(String message){
        this.timestamp = LocalDateTime.now().format(dateFormat);
        this.errorMessage = message;
        this.documentation = "https://pwr-api-dev.azurewebsites.net/swagger-ui/index.html";
    }

}
