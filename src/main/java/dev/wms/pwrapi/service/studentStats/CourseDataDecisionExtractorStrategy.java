package dev.wms.pwrapi.service.studentStats;

import dev.wms.pwrapi.dto.usos.UsosCourse;
import dev.wms.pwrapi.dto.usos.UsosSemester;
import dev.wms.pwrapi.utils.http.HttpClient;
import kotlin.Pair;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CourseDataDecisionExtractorStrategy implements CourseDataExtractionStrategy {

    @Override
    public void getCoursesDataForSemester(UsosSemester usosSemester, HttpClient client) {
        var urls = usosSemester.decisionUrls().orElse(Collections.emptySet());

        for (var link: urls) {
            var page = client.getDocument(link);
            var element = page.getElementsByClass("grey").first();
            var rows = element.getElementsByTag("tr");
            rows.remove(0);

            Map<String, Pair<Integer, String>> coursesECTSAndTeachers = processRowsToCoursesData(rows);

            updateCoursesECTSAndTeachers(usosSemester, coursesECTSAndTeachers);
        }

    }

    private Map<String, Pair<Integer, String>> processRowsToCoursesData(Elements rows) {
        Map<String, Pair<Integer, String>> coursesECTSAndTeachers = new HashMap<>();

        for (var row: rows) {
            var data = getCourseDataFromTableRow(row);
            if (data != null) {
                coursesECTSAndTeachers.put(data.getFirst(), data.getSecond());
            }
        }
        return coursesECTSAndTeachers;
    }

    private void updateCoursesECTSAndTeachers(UsosSemester usosSemester, Map<String, Pair<Integer, String>> coursesECTSAndTeachers) {
        for (UsosCourse course: usosSemester.courses()) {
            course.setTeacher(coursesECTSAndTeachers.get(course.getCode()).getSecond());
            course.setECTS(coursesECTSAndTeachers.get(course.getCode()).getFirst());
        }
    }

    private Pair<String, Pair<Integer, String>> getCourseDataFromTableRow(Element row) {
        var data = row.getElementsByTag("td");
        //data table has 2 types of rows: one with interesting for us data and one with just number of hours of mentioned courses,
        // so we need to take only this first ones
        if (data.size() > 10) {
            var ECTS = (int) Double.parseDouble(data.get(4).text());
            var teacher = data.get(8).text();
            var courseName = Arrays.stream(data.get(1).text().split(" ")).toList();
            var groupCode = courseName.get(courseName.size()-1);
            return new Pair<>(groupCode, new Pair<>(ECTS, teacher));
        }
        return null;
    }

}
