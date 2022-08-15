package dev.wms.pwrapi.utils.jsos.exceptions;

public class TooBigOffsetException extends Exception{

    public TooBigOffsetException(){
        super("You have exceeded maximum public API offset.");
    }
    
}
