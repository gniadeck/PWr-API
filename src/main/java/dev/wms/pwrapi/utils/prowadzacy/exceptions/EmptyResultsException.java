package dev.wms.pwrapi.utils.prowadzacy.exceptions;

public class EmptyResultsException extends RuntimeException{

    public EmptyResultsException(){
        super("Brak wyników. Sprawdź poprawność zapytania");
    }

}
