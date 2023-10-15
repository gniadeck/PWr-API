package dev.wms.pwrapi.utils.generalExceptions;

import dev.wms.pwrapi.dto.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ConstraintViolationExceptionAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiException> ConstraintExceptionHandler(ConstraintViolationException ex) {
        return new ApiException("Użyłeś parametru, który nie mieści się w dozwolonym zakresie.", 400, ex).toResponseEntity();
    }

}
