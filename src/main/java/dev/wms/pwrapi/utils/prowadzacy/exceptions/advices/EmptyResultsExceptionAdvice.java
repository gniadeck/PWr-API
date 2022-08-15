package dev.wms.pwrapi.utils.prowadzacy.exceptions.advices;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.utils.generalExceptionHandling.ResponseMessageHandler;
import dev.wms.pwrapi.utils.prowadzacy.exceptions.EmptyResultsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EmptyResultsExceptionAdvice {

    @ExceptionHandler(EmptyResultsException.class)
    public ResponseEntity<String> ConstraintExceptionHandler(EmptyResultsException ex) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
