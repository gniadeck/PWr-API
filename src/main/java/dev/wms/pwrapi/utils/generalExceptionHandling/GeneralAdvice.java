package dev.wms.pwrapi.utils.generalExceptionHandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.dto.ExceptionMessagingDTO;
import dev.wms.pwrapi.utils.generalExceptions.InvalidIdException;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import dev.wms.pwrapi.utils.prowadzacy.exceptions.EmptyResultsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GeneralAdvice {

    private static final HttpHeaders headers;

    static{
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(Throwable.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public ResponseEntity<ExceptionMessagingDTO> handleGeneralException(Throwable t){
        log.info("Handling unexpected exception " + t);
        t.printStackTrace();
        return ResponseEntity.status(500).body(new ExceptionMessagingDTO(t.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionMessagingDTO> handleConstraintViolation(ConstraintViolationException e){
        return ResponseEntity.status(400).body(new ExceptionMessagingDTO(e.getMessage()));
    }

    @ExceptionHandler(EmptyResultsException.class)
    public ResponseEntity<ExceptionMessagingDTO> handleEmptyResultsException(EmptyResultsException e){
        return ResponseEntity.status(404).body(new ExceptionMessagingDTO(e.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionMessagingDTO> handleMissingParameter(MissingServletRequestParameterException e){
        return ResponseEntity.status(400).body(new ExceptionMessagingDTO(e.getMessage()));
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
