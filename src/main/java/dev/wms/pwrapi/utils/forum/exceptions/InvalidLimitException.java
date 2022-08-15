package dev.wms.pwrapi.utils.forum.exceptions;

public class InvalidLimitException extends RuntimeException {
    public InvalidLimitException(int limit){
        super("Nieprawidłowy limit: " + limit + ". Ograniczenia limitu: -1 (pobierz wszystkie) <= limit.");
    }
}
