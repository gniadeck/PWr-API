package dev.wms.pwrapi.utils.forum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryMembersNotFoundException extends RuntimeException {
    public CategoryMembersNotFoundException(String category){
        super(String.format("Nie znaleziono prowadzących w kategorii: %s, dostępne kategorie [matematycy, fizycy, " +
                "informatycy, chemicy, elektronicy, jezykowcy, sportowcy, humanisci, inni]", category));
    }
}
