package dev.wms.pwrapi.utils.edukacja.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.dto.ApiException;
import dev.wms.pwrapi.utils.edukacja.exceptions.EnrollmentAccessDeniedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EdukacjaAPIAdvice {

    @ExceptionHandler(EnrollmentAccessDeniedException.class)
    public ResponseEntity<ApiException> enrollmentAccessDeniedHandler(EnrollmentAccessDeniedException ex) throws JsonProcessingException {
        return new ApiException(ex, 403).toResponseEntity();
    }

}
