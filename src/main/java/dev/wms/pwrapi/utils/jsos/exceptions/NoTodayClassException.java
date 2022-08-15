package dev.wms.pwrapi.utils.jsos.exceptions;

public class NoTodayClassException extends Exception{

    public NoTodayClassException(){
        super("No lectures today.");
    }
    
}
