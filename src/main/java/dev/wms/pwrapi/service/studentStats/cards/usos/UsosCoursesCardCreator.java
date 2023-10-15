package dev.wms.pwrapi.service.studentStats.cards.usos;

import dev.wms.pwrapi.domain.studentstats.StudentStatsCategory;
import dev.wms.pwrapi.dto.usos.UsosCourse;
import dev.wms.pwrapi.dto.usos.UsosSemester;
import dev.wms.pwrapi.dto.usos.UsosStudies;
import dev.wms.pwrapi.model.studentStats.*;
import dev.wms.pwrapi.service.internationalization.LocalizedMessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class UsosCoursesCardCreator implements UsosCardCreator {

    private final LocalizedMessageService msgService;

    @Override
    public List<StudentStatsObject> getAlwaysPresentCards(List<UsosStudies> userStudies) {
        return List.of(createBestCourseCard(userStudies), createWorstCourseCard(userStudies),
                createMarkGraphCard(userStudies), createMostMarksCard(userStudies), totalECTSPointsCard(userStudies));
    }

    @Override
    public List<Optional<StudentStatsObject>> getOptionalCards(List<UsosStudies> userStudies) {
        return List.of(createGreatMarkCourseCard(userStudies), createFailedCoursesCard(userStudies), createECTSPointsChartCard(userStudies));
    }

    private List<UsosCourse> getCoursesWithMark(List<UsosStudies> usosStudies, BigDecimal mark) {
        return usosStudies.stream()
                .map(UsosStudies::semesters)
                .flatMap(Collection::stream)
                .map(UsosSemester::courses)
                .flatMap(Collection::stream)
                .filter(course -> course.getMark().equals(mark))
                .toList();
    }

    private StudentStatsObject totalECTSPointsCard(List<UsosStudies> userStudies) {
        var totalECTSPoints = userStudies.stream()
                .map(UsosStudies::semesters)
                .flatMap(Collection::stream)
                .map(UsosSemester::courses)
                .flatMap(Collection::stream)
                .map(UsosCourse::getECTS)
                .reduce(0, Integer::sum);

        return StudentStatsText.asObject(StudentStatsCategory.COURSES,
                msgService.getMessageFromContext("msg.StudentStatsService.ects-balance"),
                null,
                totalECTSPoints.toString());
    }

    private Optional<StudentStatsObject> createFailedCoursesCard(List<UsosStudies> usosStudies) {
        var failedCourses = getCoursesWithMark(usosStudies, BigDecimal.valueOf(2.0));
        if (!failedCourses.isEmpty()) {
            return Optional.of(StudentStatsText.asObject(StudentStatsCategory.COURSES,
                    msgService.getMessageWithArgsFromContext("msg.UsosCoursesCardCreator.how-many-failed", failedCourses.size()),
                    msgService.getMessageWithArgsFromContext("msg.UsosCoursesCardCreator.which-failed", toCourseList(failedCourses)),
                    String.valueOf(failedCourses.size())));
        }
        return Optional.empty();
    }

    private Optional<StudentStatsObject> createGreatMarkCourseCard(List<UsosStudies> usosStudies) {
        var greatMarks = getCoursesWithMark(usosStudies, BigDecimal.valueOf(5.5));
        if (!greatMarks.isEmpty()) {
            return Optional.of(StudentStatsText.asObject(StudentStatsCategory.COURSES,
                    msgService.getMessageWithArgsFromContext("msg.UsosCoursesCardCreator.how-many-excellent-grades"),
                    msgService.getMessageWithArgsFromContext("msg.UsosCoursesCardCreator.best-subjects", toCourseList(greatMarks)),
                    String.valueOf(greatMarks.size())));
        }
        return Optional.empty();
    }

    private String toCourseList(List<UsosCourse> courses) {
        return courses.stream().map(UsosCourse::getName).map(String::strip).collect(Collectors.joining(", "));
    }

    private StudentStatsObject createWorstCourseCard(List<UsosStudies> userStudies) {
        return createCourseCard(userStudies, Comparator.comparing(UsosCourse::getMark).reversed(),
                msgService.getMessageFromContext("msg.UsosCoursesCardCreator.worst-grade"), course -> course.getMark().toString());
    }

    private StudentStatsObject createCourseCard(List<UsosStudies> userStudies, Comparator<UsosCourse> courseComparator,
                                                String titleToFormat, Function<UsosCourse, String> valueGenerator) {
        var bestMark = userStudies.stream()
                .map(UsosStudies::semesters)
                .flatMap(List::stream)
                .map(UsosSemester::courses)
                .flatMap(List::stream)
                .max(courseComparator);

        return StudentStatsObject.builder()
                .category(StudentStatsCategory.COURSES)
                .content(StudentStatsText.builder()
                        .title(titleToFormat.formatted(bestMark.orElseThrow().getName().trim()))
                        .value(valueGenerator.apply(bestMark.orElseThrow()))
                        .build())
                .build();
    }

    private StudentStatsObject createBestCourseCard(List<UsosStudies> userStudies) {
        return createCourseCard(userStudies, Comparator.comparing(UsosCourse::getMark),
                msgService.getMessageFromContext("msg.UsosCoursesCardCreator.best-grade"), course -> course.getMark().toString());
    }

    private Optional<StudentStatsObject> createECTSPointsChartCard(List<UsosStudies> userStudies) {
        if (numberOfSemesters(userStudies) <= 1) {
            return Optional.empty();
        }

        List<StudentStatsChartValue> values = createECTSChartValues(userStudies);

        return Optional.ofNullable(buildECTSChart(values));
    }

    private List<StudentStatsChartValue> createECTSChartValues(List<UsosStudies> userStudies) {
        List<StudentStatsChartValue> values = new ArrayList<>();

        userStudies.stream()
                .flatMap(studies -> studies.semesters().stream())
                .forEach(semester -> {
                    int totalECTS = semester.courses().stream()
                            .mapToInt(UsosCourse::getECTS)
                            .sum();
                    values.add(new StudentStatsChartValue((double) totalECTS, semester.name()));
                });

        Collections.reverse(values);

        return values;
    }

    private StudentStatsObject buildECTSChart(List<StudentStatsChartValue> values) {
        return StudentStatsObject.builder()
                .category(StudentStatsCategory.COURSES)
                .content(StudentStatsChart.builder()
                        .chartType(StudentStatsChartType.LINE)
                        .title(msgService.getMessageFromContext("msg.UsosCoursesCardCreator.ects-graph"))
                        .values(values)
                        .build())
                .build();
    }


    private Integer numberOfSemesters(List<UsosStudies> userStudies) {
        return userStudies.stream()
                .mapToInt(UsosStudies::numberOfSemesters)
                .sum();
    }

    private StudentStatsObject createMostMarksCard(List<UsosStudies> usosStudies) {
        var marksAndStudies = getMarksPerCourse(usosStudies);
        var mostOftenReceivedMark = marksAndStudies.entrySet()
                .stream().max(Comparator.comparing(entry -> entry.getValue().size()))
                .stream().findFirst().orElseThrow();

        return StudentStatsText.asObject(StudentStatsCategory.COURSES,
                msgService.getMessageWithArgsFromContext(
                        "msg.UsosCoursesCardCreator.highest-frequency-grade"
                ),
                msgService.getMessageWithArgsFromContext(
                        "msg.UsosCoursesCardCreator.highest-frequency-grade-courses",
                        mostOftenReceivedMark.getValue().stream()
                                .map(course -> course.getName().trim())
                                .collect(Collectors.joining(", "))
                ),
                mostOftenReceivedMark.getKey().toString()
        );
    }

    private StudentStatsObject createMarkGraphCard(List<UsosStudies> usosStudies) {
        Map<BigDecimal, List<UsosCourse>> marksPerCourse = getMarksPerCourse(usosStudies);
        return StudentStatsObject.builder()
                .category(StudentStatsCategory.COURSES)
                .content(StudentStatsChart.builder()
                        .chartType(StudentStatsChartType.BAR)
                        .title(msgService.getMessageFromContext("msg.UsosCoursesCardCreator.grade-graph"))
                        .values(toStudentStatsChartValue(marksPerCourse))
                        .build())
                .build();
    }

    private List<StudentStatsChartValue> toStudentStatsChartValue(Map<BigDecimal, List<UsosCourse>> marksPerCourse) {
        return marksPerCourse.entrySet()
                .stream().map(entry -> StudentStatsChartValue.of((double) entry.getValue().size(), entry.getKey().toString()))
                .sorted(Comparator.comparingDouble(chartValue -> Double.parseDouble(chartValue.getLabel())))
                .toList();
    }

    private Map<BigDecimal, List<UsosCourse>> getMarksPerCourse(List<UsosStudies> usosStudies) {
        return Stream.of(2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5)
                .map(BigDecimal::valueOf)
                .map(mark -> new AbstractMap.SimpleEntry<>(mark, getCoursesWithMark(usosStudies, mark)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


}
