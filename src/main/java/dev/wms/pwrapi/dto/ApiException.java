package dev.wms.pwrapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.wms.pwrapi.utils.config.ExceptionReporter;
import dev.wms.pwrapi.utils.properties.PropertiesProvider;
import lombok.Value;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

/**
 * This class is used for formatting exceptions that are thrown by our API. It will be automatically reported
 * on creation to listening exception handling systems
 */
@Value
@JsonIgnoreProperties({"stackTrace", "cause", "localizedMessage", "suppressed", "message"})
public class ApiException extends RuntimeException{

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
    String documentation;
    String supportCode;
    @JsonIgnore
    int responseCode;

    public ApiException(String message, Throwable t){
        this(message, 500, t);
    }

    public ApiException(Throwable t) {
        this(t.getMessage(), 500, t);
    }

    public ApiException(String message) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.responseCode = 500;
        this.documentation = getDocumentationReference();
        this.supportCode = report(this);
    }

    public ApiException(Throwable t, int responseCode) {
        this(t.getMessage(), responseCode, t);
    }

    public ApiException(String errorMessage, int responseCode, Throwable t) {
        super(errorMessage);
        this.timestamp = LocalDateTime.now();
        this.responseCode = responseCode;
        this.documentation = getDocumentationReference();
        this.supportCode = report(t);
    }

    private String report(Throwable t){
        return ExceptionReporter.report(t);
    }

    public ResponseEntity<ApiException> toResponseEntity(){
        return ResponseEntity.status(responseCode).body(this);
    }

    private String getDocumentationReference() {
        return PropertiesProvider.getDocumentationReferenceUrl();
    }

    @JsonProperty("errorMessage")
    public String getErrorMessage() {
        return super.getMessage();
    }
}
