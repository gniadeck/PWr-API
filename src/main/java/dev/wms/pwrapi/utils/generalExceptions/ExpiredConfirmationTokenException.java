package dev.wms.pwrapi.utils.generalExceptions;

public class ExpiredConfirmationTokenException extends RuntimeException {

    public ExpiredConfirmationTokenException(){
        super("Token stracił ważność.");
    }

    public ExpiredConfirmationTokenException(String msg){
        super(msg);
    }
}
