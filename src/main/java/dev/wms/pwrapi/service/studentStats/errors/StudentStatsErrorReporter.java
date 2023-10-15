package dev.wms.pwrapi.service.studentStats.errors;

import dev.wms.pwrapi.utils.config.SentryReporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StudentStatsErrorReporter {

    private final SentryReporter sentryReporter;

    public void reportCardCreationError(String message){
        sentryReporter.captureMessage(message);
    }

    public LocalDate reportAndGetUndefinedDate(String message){
        reportCardCreationError(message);
        return LocalDate.now();
    }
}
