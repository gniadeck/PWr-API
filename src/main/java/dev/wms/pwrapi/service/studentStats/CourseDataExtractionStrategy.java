package dev.wms.pwrapi.service.studentStats;

import dev.wms.pwrapi.dto.usos.UsosSemester;
import dev.wms.pwrapi.utils.http.HttpClient;

public interface CourseDataExtractionStrategy {
    void getCoursesDataForSemester(UsosSemester usosSemester, HttpClient client);
}
