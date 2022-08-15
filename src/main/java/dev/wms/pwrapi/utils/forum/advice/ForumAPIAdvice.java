package dev.wms.pwrapi.utils.forum.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.wms.pwrapi.utils.forum.exceptions.*;
import dev.wms.pwrapi.utils.generalExceptionHandling.ResponseMessageHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ForumAPIAdvice {

    private static final HttpHeaders headers;

    static{
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<String> reviewNotFoundExceptionHandler(ReviewNotFoundException ex) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON(ex.getMessage());
        return new ResponseEntity<>(response, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeacherNotFoundByIdException.class)
    public ResponseEntity<String> teacherNotFoundExceptionHandler(TeacherNotFoundByIdException ex) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON(ex.getMessage());
        return new ResponseEntity<>(response, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeacherNotFoundByFullNameException.class)
    public ResponseEntity<String> teacherNotFoundByFullNameExceptionHandler(TeacherNotFoundByFullNameException ex) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON(ex.getMessage());
        return new ResponseEntity<>(response, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidLimitException.class)
    public ResponseEntity<String> invalidLimitExceptionHandler(InvalidLimitException ex) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON(ex.getMessage());
        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryMembersNotFoundException.class)
    public ResponseEntity<String> categoryMembersNotFoundExceptionHandled(CategoryMembersNotFoundException ex) throws JsonProcessingException {
        String response = ResponseMessageHandler.createResponseMessageJSON(ex.getMessage());
        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }

}
