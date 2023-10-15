package dev.wms.pwrapi.utils.config;


import org.springframework.stereotype.Component;


@Component
public class ExceptionReporter {

    private static ExceptionHandler exceptionHandler;

    public ExceptionReporter(SentryReporter sentryReporter) {
        exceptionHandler = sentryReporter;
    }

    public static String report(Throwable throwable){
        return exceptionHandler.report(throwable);
    }


}
