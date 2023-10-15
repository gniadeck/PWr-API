package dev.wms.pwrapi.utils.common;

import java.net.MalformedURLException;
import java.net.URL;

public final class URLValidator {

    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

}
