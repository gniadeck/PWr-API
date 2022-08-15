package dev.wms.pwrapi.utils.eportal;

import dev.wms.pwrapi.dto.eportal.sections.EportalElementType;

public class EportalGeneralUtils {

    /**
     * Converts ePortal's URL to EportalElementType enum for nice output formatting
     * @param url ePortal url for resource
     * @return EportalElementTypeEnum corresponding to known types, INNE for unknown types
     */
    public static EportalElementType determineElementTypeFromUrl(String url) {

        if (url.contains("assign"))
            return EportalElementType.ZADANIE;
        if (url.contains("pdf"))
            return EportalElementType.PDF;
        if (url.contains("powerpoint"))
            return EportalElementType.PREZENTACJA;
        if (url.contains("core"))
            return EportalElementType.PLIK;
        if (url.contains("url"))
            return EportalElementType.LINK;
        if (url.contains("page"))
            return EportalElementType.LINK;
        if (url.contains("archive"))
            return EportalElementType.ARCHIWUM;
        if (url.contains("reservation"))
            return EportalElementType.REZERWACJA;
        if (url.contains("quiz"))
            return EportalElementType.QUIZ;

        return EportalElementType.INNE;
    }

}
