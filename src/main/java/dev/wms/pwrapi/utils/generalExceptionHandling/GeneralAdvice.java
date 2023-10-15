package dev.wms.pwrapi.utils.generalExceptionHandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.dto.ApiException;
import dev.wms.pwrapi.utils.generalExceptions.*;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
@Slf4j
public class GeneralAdvice {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiException> handleGeneralException(Throwable t) {
        t.printStackTrace();
        return new ApiException(t).toResponseEntity();
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiException> handleFeignException(FeignException ex) {
        return new ApiException(ex, ex.status()).toResponseEntity();
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ApiException> jsonProcessingExceptionHandler(JsonProcessingException ex) {
        return new ApiException("Wystąpił błąd, spróbuj ponownie lub skontaktuj się z właścicielem serwisu.", ex).toResponseEntity();
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ApiException> loginExceptionHandler(LoginException ex){
        return new ApiException(ex, 401).toResponseEntity();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiException> conversionFailedException(MethodArgumentTypeMismatchException ex){
        return new ApiException(ex, 400).toResponseEntity();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiException> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        return new ApiException(ex, 404).toResponseEntity();
    }

    @ExceptionHandler(ExpiredConfirmationTokenException.class)
    public ResponseEntity<ApiException> expiredConfirmationTokenExceptionHandler(ExpiredConfirmationTokenException ex){
        return new ApiException(ex, 400).toResponseEntity();
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiException> userDisabledExceptionHandler(DisabledException ex){
        return new ApiException(ex, 401).toResponseEntity();
    }

    @ExceptionHandler(SystemTimeoutException.class)
    public ResponseEntity<ApiException> systemTimeoutExceptionHandler(SystemTimeoutException exception) {
        return new ApiException(exception, 502).toResponseEntity();
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ApiException> rateLimitExceptionHandler(RateLimitException exception) {
        return new ApiException(exception, 429).toResponseEntity();
    }
}
