package dev.wms.pwrapi.utils.forum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TeacherNotFoundException extends RuntimeException {
    public TeacherNotFoundException(Long teacherId){
        super(String.format("Prowadzący o id '%d' nie istnieje.", teacherId));
    }
}
