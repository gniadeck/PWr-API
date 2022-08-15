package dev.wms.pwrapi.utils.eportal.exceptions;

public class WrongCourseIdException extends RuntimeException{


    public WrongCourseIdException(){
        super("You don't have access to course with given ID.");
    }
    
}
