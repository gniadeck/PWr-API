package dev.wms.pwrapi.utils.eportal.advice;

import dev.wms.pwrapi.dto.ApiException;
import dev.wms.pwrapi.utils.eportal.exceptions.WrongCourseIdException;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EportalAPIAdvice {

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ApiException> handleException(LoginException e) {
        return new ApiException("Błędny login, lub hasło.", 401, e).toResponseEntity();
    }

    @ExceptionHandler(WrongCourseIdException.class)
    public ResponseEntity<ApiException> handleException(WrongCourseIdException e) {
        return new ApiException("Nie posiadasz uprawnień do tego kursu.", 401, e).toResponseEntity();
    }
}
