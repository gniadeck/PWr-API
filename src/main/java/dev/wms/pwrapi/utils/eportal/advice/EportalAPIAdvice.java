package dev.wms.pwrapi.utils.eportal.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.dto.ExceptionMessagingDTO;
import dev.wms.pwrapi.utils.eportal.exceptions.WrongCourseIdException;
import dev.wms.pwrapi.utils.generalExceptionHandling.ResponseMessageHandler;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EportalAPIAdvice {

    private static final HttpHeaders headers;

    static{
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleException(LoginException e) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON("Błędny login, lub hasło.");
        return new ResponseEntity<>(response, headers, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WrongCourseIdException.class)
    public ResponseEntity<String> handleException(WrongCourseIdException e) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON("Nie posiadasz uprawnień do tego kursu.");
        return new ResponseEntity<>(response, headers, HttpStatus.UNAUTHORIZED);
    }
}
