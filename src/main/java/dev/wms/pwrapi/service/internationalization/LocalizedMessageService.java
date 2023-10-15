package dev.wms.pwrapi.service.internationalization;

/**
 * Service that provides localized messages located in files named
 * messages_{locale}.properties.
 * */
public interface LocalizedMessageService {

    /**
     * @param key - key for getting a localized message from file named
     *            messages_{locale}.properties.
     * @param language - language of message. Specifies {locale} property.
     * @return a localized message.
     *  <p>
     *      Example:
     *      <br><b>language</b>: SupportedLanguage.ENGLISH</br>
     *      <br><b>key</b>: "hello"</br>
     *      <br><b>Looks for content in messages_en.properties</b>: hello=Hello World!</br>
     *      <br><b>Result</b>: Hello World!</br>
     * </p>
     * */
    String getMessage(String key, SupportedLanguage language);

    /**
     * @param key - key for getting a localized message from file named
     *            messages_{locale}.properties
     * @param language - language of message. Specifies {locale} property
     * @param args - arguments that will be put to message.
     * @return a localized message.
     *
     *  <p>
     *      Example:
     *      <br><b>language</b>: SupportedLanguage.ENGLISH</br>
     *      <br><b>key</b>: "hello"</br>
     *      <br><b>args</b>: "World"</br>
     *      <br>Looks for content in <b>messages_en.properties</b>: hello=Hello {0}!</br>
     *      <br><b>Result</b>: Hello World!</br>
     * </p>
     * */
    String getMessageWithArgs(String key, SupportedLanguage language, Object... args);

    /**
     * Gets message based on locale stored in LocaleContext.
     * Note that the context is local to current thread.
     *
     * @param key - key for getting a localized message from file named
     *           messages_{locale}.properties
     *
     * */
    String getMessageFromContext(String key);

    /**
     * Gets message based on locale stored in LocaleContext.
     * Note that the context is local to current thread.
     *
     * @param key - key for getting a localized message from file named
     *           messages_{locale}.properties
     * @param args - arguments that will be put to message.
     *
     * */
    String getMessageWithArgsFromContext(String key, Object... args);


    /**
     * @return language stored in session or default if there is none.
     * */
    SupportedLanguage getLanguageFromContextOrDefault();
}
