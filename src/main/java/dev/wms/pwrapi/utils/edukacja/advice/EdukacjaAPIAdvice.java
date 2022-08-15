package dev.wms.pwrapi.utils.edukacja.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.utils.edukacja.exceptions.EnrollmentAccessDeniedException;
import dev.wms.pwrapi.utils.generalExceptionHandling.ResponseMessageHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EdukacjaAPIAdvice {

    private static final HttpHeaders headers;

    static {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(EnrollmentAccessDeniedException.class)
    public ResponseEntity<String> enrollmentAccessDeniedHandler(EnrollmentAccessDeniedException ex) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON(ex.getMessage());
        return new ResponseEntity<>(response, headers, HttpStatus.FORBIDDEN);
    }

}
