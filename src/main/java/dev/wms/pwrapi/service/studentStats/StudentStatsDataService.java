package dev.wms.pwrapi.service.studentStats;

import dev.wms.pwrapi.model.studentStats.StudentStatsObject;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface StudentStatsDataService {
    /*
    Get data for student stats purposes
     */
    @Async
    CompletableFuture<List<StudentStatsObject>> getData(String login, String password, LocaleContext localeContext);

}
