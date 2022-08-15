package dev.wms.pwrapi.eportal;

import dev.wms.pwrapi.dto.eportal.sections.EportalElementType;
import dev.wms.pwrapi.utils.eportal.EportalGeneralUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EportalUtilsClassesTests {


    @Test
    public void eportalGeneralUtilsClassShouldReturnInneIfForeignValuePresent(){

        List<String> imposibleElementTypes = List.of("iAmNotAType", "meNeither", "lolIAmNotAType");
        imposibleElementTypes = imposibleElementTypes.stream()
                .map(this::makeUrlFor)
                .collect(Collectors.toList());


        for(String immposibleType : imposibleElementTypes){
            assertEquals(EportalElementType.INNE, EportalGeneralUtils.determineElementTypeFromUrl(immposibleType));
        }

    }


    @Test
    public void eportalGeneralUtilsClassShouldReturnValueIfPresetInUrl(){

        List<String> possibleElementTypes = List.of("assign", "pdf", "powerpoint", "core", "url", "page", "archive","reservation", "quiz","other");

        possibleElementTypes = possibleElementTypes.stream()
                .map(this::makeUrlFor)
                .collect(Collectors.toList());

        assertEquals(EportalElementType.ZADANIE, EportalGeneralUtils.determineElementTypeFromUrl(possibleElementTypes.get(0)));
        assertEquals(EportalElementType.PDF, EportalGeneralUtils.determineElementTypeFromUrl(possibleElementTypes.get(1)));
        assertEquals(EportalElementType.PREZENTACJA, EportalGeneralUtils.determineElementTypeFromUrl(possibleElementTypes.get(2)));
        assertEquals(EportalElementType.PLIK, EportalGeneralUtils.determineElementTypeFromUrl(possibleElementTypes.get(3)));
        assertEquals(EportalElementType.LINK, EportalGeneralUtils.determineElementTypeFromUrl(possibleElementTypes.get(4)));
        assertEquals(EportalElementType.LINK, EportalGeneralUtils.determineElementTypeFromUrl(possibleElementTypes.get(5)));
        assertEquals(EportalElementType.ARCHIWUM, EportalGeneralUtils.determineElementTypeFromUrl(possibleElementTypes.get(6)));
        assertEquals(EportalElementType.REZERWACJA, EportalGeneralUtils.determineElementTypeFromUrl(possibleElementTypes.get(7)));
        assertEquals(EportalElementType.QUIZ, EportalGeneralUtils.determineElementTypeFromUrl(possibleElementTypes.get(8)));


    }

    private String makeUrlFor(String containing){

        String urlPrefix = "http://iAmAUrl.com/someStuff/someStuff";
        String urlPostfix = "/yeahPostfix/hello";

        return urlPrefix + containing + urlPostfix;
    }


}
