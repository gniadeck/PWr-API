package dev.wms.pwrapi.utils.edukacja.exceptions;

public class EnrollmentAccessDeniedException extends RuntimeException {

    public EnrollmentAccessDeniedException(){
        super("Prawo do zapisów w aktualnym cyklu zapisowym nie zostało przyznane, odczyt grup niemożliwy.");
    }
}
