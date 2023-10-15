package dev.wms.pwrapi.utils.generalExceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String msg){
        super(msg);
    }
}
