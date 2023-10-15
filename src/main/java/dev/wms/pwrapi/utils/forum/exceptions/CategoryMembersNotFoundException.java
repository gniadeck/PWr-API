package dev.wms.pwrapi.utils.forum.exceptions;

import dev.wms.pwrapi.utils.forum.consts.Category;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryMembersNotFoundException extends RuntimeException {
    public CategoryMembersNotFoundException(Category category){
        super(String.format("Nie znaleziono prowadzących w kategorii: %s, dostępne kategorie %s", category,
                Arrays.toString(Category.values())));
    }
}
