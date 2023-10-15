package dev.wms.pwrapi.utils.prowadzacy.exceptions.advices;

import dev.wms.pwrapi.dto.ApiException;
import dev.wms.pwrapi.utils.prowadzacy.exceptions.EmptyResultsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EmptyResultsExceptionAdvice {

    @ExceptionHandler(EmptyResultsException.class)
    public ResponseEntity<ApiException> ConstraintExceptionHandler(EmptyResultsException ex) {
        return new ApiException(ex, 404).toResponseEntity();
    }

}
