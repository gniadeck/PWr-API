package dev.wms.pwrapi.utils.forum.exceptions;

public class TeacherNotFoundByIdException extends RuntimeException {
    public TeacherNotFoundByIdException(int teacherId){
        super("Prowadzący o id: " + teacherId + " nie istnieje.");
    }

}
