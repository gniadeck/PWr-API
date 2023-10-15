package dev.wms.pwrapi.utils.generalExceptions;

public class RateLimitException extends RuntimeException {

    public RateLimitException() {
        super();
    }

    public RateLimitException(String message) {
        super(message);
    }
}
