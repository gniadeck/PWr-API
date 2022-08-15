package dev.wms.pwrapi.utils.parking.exceptions;

public class WrongResponseCode extends RuntimeException{
    
    public WrongResponseCode(){
        super("The university server returned wrong response code. Please contact the administrator.");
    }

}
