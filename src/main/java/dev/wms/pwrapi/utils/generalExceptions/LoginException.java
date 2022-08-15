package dev.wms.pwrapi.utils.generalExceptions;

public class LoginException extends RuntimeException {

    public LoginException(){
        super("Nieprawidłowy login lub hasło.");
    }
    
}
