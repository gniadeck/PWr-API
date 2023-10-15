package dev.wms.pwrapi.dao.usos;

import dev.wms.pwrapi.dto.usos.UsosSemester;
import dev.wms.pwrapi.dto.usos.UsosStudies;
import dev.wms.pwrapi.service.studentStats.CourseDataDecisionExtractorStrategy;
import dev.wms.pwrapi.service.studentStats.CourseDataDetailsExtractorStrategy;
import dev.wms.pwrapi.service.studentStats.CourseDataExtractionStrategy;
import dev.wms.pwrapi.utils.http.HttpClient;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;

import java.util.*;

@RequiredArgsConstructor
public class UsosMarksProxy {

    private final HttpClient client;
    private final CourseDataExtractionStrategy decisionStrategy = new CourseDataDecisionExtractorStrategy();
    private final CourseDataExtractionStrategy detailsStrategy = new CourseDataDetailsExtractorStrategy();

    public void enhanceWithMarks(List<UsosStudies> studies) {
        studies.stream()
                .map(UsosStudies::semesters)
                .flatMap(Collection::stream)
                .filter(semester -> semester.decisionUrls().isPresent())
                .forEach(semester -> applyStrategy(semester, decisionStrategy));

        studies.stream()
                .map(UsosStudies::semesters)
                .flatMap(Collection::stream)
                .filter(semester -> semester.decisionUrls().isEmpty())
                .forEach(semester -> applyStrategy(semester, detailsStrategy));
    }

    private void applyStrategy(UsosSemester semster, CourseDataExtractionStrategy strategy) {
        strategy.getCoursesDataForSemester(semster, client);
    }
}
