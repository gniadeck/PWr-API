package dev.wms.pwrapi.utils.forum.exceptions;

public class CategoryMembersNotFoundException extends RuntimeException {
    public CategoryMembersNotFoundException(String category){
        super("Nie znaleziono prowadzących w kategorii: " + category +
                ". Lista dostępnych kategorii [matematycy, fizycy, informatycy, chemicy, elektronicy, jezykowcy, sportowcy, humanisci, inni].");
    }
}
