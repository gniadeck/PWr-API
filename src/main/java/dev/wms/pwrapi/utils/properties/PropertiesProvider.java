package dev.wms.pwrapi.utils.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesProvider {

    private static String documentationReferenceUrl;

    public PropertiesProvider(@Value("${api.documentation.reference}") String documentationReference) {
        documentationReferenceUrl = documentationReference;
    }

    public static String getDocumentationReferenceUrl() {
        return documentationReferenceUrl;
    }

}
