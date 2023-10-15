package dev.wms.pwrapi.utils.generalExceptions;

public class SystemTimeoutException extends RuntimeException {
    public SystemTimeoutException(){
        super("Our API, or university system is under heavy load. Please try again.");
    }
}