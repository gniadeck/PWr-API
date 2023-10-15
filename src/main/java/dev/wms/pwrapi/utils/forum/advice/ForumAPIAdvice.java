package dev.wms.pwrapi.utils.forum.advice;

import dev.wms.pwrapi.dto.ApiException;
import dev.wms.pwrapi.utils.forum.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ForumAPIAdvice {

    private static final HttpHeaders headers;

    static {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler({ReviewNotFoundException.class, TeacherNotFoundByIdException.class, TeacherNotFoundByFullNameException.class})
    public ResponseEntity<ApiException> reviewNotFoundExceptionHandler(ReviewNotFoundException ex) {
        return new ApiException(ex, 404).toResponseEntity();
    }

    @ExceptionHandler({InvalidLimitException.class, CategoryMembersNotFoundException.class})
    public ResponseEntity<ApiException> invalidLimitExceptionHandler(InvalidLimitException ex) {
        return new ApiException(ex, 400).toResponseEntity();
    }

}
