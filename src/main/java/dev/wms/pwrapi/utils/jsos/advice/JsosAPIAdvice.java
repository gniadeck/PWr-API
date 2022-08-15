package dev.wms.pwrapi.utils.jsos.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.utils.generalExceptionHandling.ResponseMessageHandler;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import dev.wms.pwrapi.utils.jsos.exceptions.NoTodayClassException;
import dev.wms.pwrapi.utils.jsos.exceptions.TooBigOffsetException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JsosAPIAdvice {

    private static final HttpHeaders headers;

    static{
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(TooBigOffsetException.class)
    public ResponseEntity<String> handleException(TooBigOffsetException e) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON("Przekroczyłeś(aś) maksymalną wartość przesunięcia dla publicznego API.");
        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleException(LoginException e) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON("Błędny login, lub hasło.");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(NoTodayClassException.class)
    public ResponseEntity<String> handleException(NoTodayClassException e) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON("Brak zajęć.");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }
}
