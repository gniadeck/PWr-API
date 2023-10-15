package dev.wms.pwrapi.utils.jsos.advice;

import dev.wms.pwrapi.dto.ApiException;
import dev.wms.pwrapi.utils.jsos.exceptions.NoTodayClassException;
import dev.wms.pwrapi.utils.jsos.exceptions.TooBigOffsetException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JsosAPIAdvice {

    @ExceptionHandler(TooBigOffsetException.class)
    public ResponseEntity<ApiException> handleException(TooBigOffsetException e) {
        return new ApiException("Przekroczyłeś(aś) maksymalną wartość przesunięcia dla publicznego API.", 400, e).toResponseEntity();
    }

    @ExceptionHandler(NoTodayClassException.class)
    public ResponseEntity<ApiException> handleException(NoTodayClassException e) {
        return new ApiException("Brak zajęć.", 401, e).toResponseEntity();
    }
}
