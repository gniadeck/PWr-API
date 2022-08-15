package dev.wms.pwrapi.utils.generalExceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.utils.generalExceptionHandling.ResponseMessageHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ConstraintViolationExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String ConstraintExceptionHandler(ConstraintViolationException ex) throws JsonProcessingException {
        return ResponseMessageHandler.createResponseMessageJSON(ex.getMessage()
                + " Użyłeś parametru, który nie mieści się w dozwolonym zakresie.");
    }


}
