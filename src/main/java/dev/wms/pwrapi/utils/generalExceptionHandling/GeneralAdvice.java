package dev.wms.pwrapi.utils.generalExceptionHandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.utils.generalExceptions.InvalidIdException;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GeneralAdvice {

    private static final HttpHeaders headers;

    static{
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> loginExceptionHandler(LoginException ex) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON(ex.getMessage());
        return new ResponseEntity<>(response, headers, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<String> invalidIdExceptionHandler(InvalidIdException ex) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON(ex.getMessage());
        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> jsonProcessingExceptionHandler(JsonProcessingException ex) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON("Wystąpił błąd, spróbuj ponownie lub skontaktuj się z właścicielem serwisu.");
        return new ResponseEntity<>(response, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
