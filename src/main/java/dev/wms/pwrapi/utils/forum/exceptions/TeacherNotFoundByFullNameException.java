package dev.wms.pwrapi.utils.forum.exceptions;

public class TeacherNotFoundByFullNameException extends RuntimeException {
    public TeacherNotFoundByFullNameException(String firstName, String lastName){
        super("Prowadzący o imieniu: " + firstName + " " + lastName + " nie istnieje.");
    }
}
