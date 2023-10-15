package dev.wms.pwrapi.service.internationalization;

import java.util.Locale;

public enum SupportedLanguage {

    PL(new Locale("pl")), EN(new Locale("en"));

    private final Locale locale;

    SupportedLanguage(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public static SupportedLanguage valueOfLocaleLanguage(Locale locale) {
        for (SupportedLanguage language : values()) {
            if (language.getLocale().getLanguage().equals(locale.getLanguage())) {
                return language;
            }
        }
        return null;
    }
}
