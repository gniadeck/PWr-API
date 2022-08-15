package dev.wms.pwrapi.utils.generalExceptions;

public class InvalidIdException extends RuntimeException {

    public InvalidIdException(long id){
        super("Nieprawidłowe id: " + id + ".");
    }
}
