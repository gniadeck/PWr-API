package dev.wms.pwrapi.service.studentStats.cards.usos;

import dev.wms.pwrapi.domain.studentstats.StudentStatsCategory;
import dev.wms.pwrapi.dto.usos.UsosCourse;
import dev.wms.pwrapi.dto.usos.UsosSemester;
import dev.wms.pwrapi.dto.usos.UsosStudies;
import dev.wms.pwrapi.model.studentStats.*;
import dev.wms.pwrapi.service.forum.ForumServiceImpl;
import dev.wms.pwrapi.service.internationalization.LocalizedMessageService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsosTeacherRatingCardCreator implements UsosCardCreator {

    private final ForumServiceImpl forumService;
    private final LocalizedMessageService msgService;
    private static final Pattern USOS_TEACHER_LAST_NAME_PATTERN = Pattern.compile("\\w+$");
    private static final Pattern USOS_TEACHER_FULL_NAME_PATTERN = Pattern.compile("[A-Za-zÀ-ȕ ]+\\W+\\w+$");

    // TODO add cards for most reviews
    @Override
    public List<Optional<StudentStatsObject>> getOptionalCards(List<UsosStudies> usosStudies) {
        Map<String, Double> availableTeacherRatings = getAvailableTeacherRatings(usosStudies);
        return List.of(
                bestRatedTeacher(availableTeacherRatings),
                worstRatedTeacher(availableTeacherRatings),
                reviewGraph(availableTeacherRatings),
                averageTeacherMark(availableTeacherRatings)
        );
    }

    private Optional<StudentStatsObject> averageTeacherMark(Map<String, Double> availableTeacherRatings) {
        if(availableTeacherRatings.isEmpty()) return Optional.empty();

        return Optional.of(StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.studentstats.averageteachermark.title"))
                        .value(String.valueOf(getAverageRating(availableTeacherRatings)).subSequence(0, 3).toString())
                        .build())
                .build());
    }

    private double getAverageRating(Map<String, Double> availableTeacherRatings) {
        return availableTeacherRatings.values().stream()
                .mapToDouble(number -> number)
                .average().orElse(0.0);
    }

    private Optional<StudentStatsObject> reviewGraph(Map<String, Double> availableTeacherRatings) {
        return Optional.of(availableTeacherRatings)
                .filter(Map::isEmpty)
                .map(this::createReviewCard);
    }

    @NotNull
    private StudentStatsObject createReviewCard(Map<String, Double> availableTeacherRatings) {
        return StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsChart.builder()
                        .chartType(StudentStatsChartType.BAR)
                        .title(msgService.getMessageFromContext("msg.studentstats.reviewgraph.title"))
                        .values(getChartValues(availableTeacherRatings))
                        .build())
                .build();
    }

    private List<StudentStatsChartValue> getChartValues(Map<String, Double> availableTeacherRatings) {
        return availableTeacherRatings.entrySet().stream()
                .map(this::createChartValue)
                .toList();
    }

    private StudentStatsChartValue createChartValue(Map.Entry<String, Double> entry) {
        return StudentStatsChartValue.builder()
                .label(entry.getKey())
                .value(entry.getValue())
                .build();
    }

    private Optional<StudentStatsObject> worstRatedTeacher(Map<String, Double> availableTeacherRatings) {
        return teacherByComparator(availableTeacherRatings, "msg.studentstats.worstratedteacher.title",
                "msg.studentstats.worstratedteacher.subtitle", Map.Entry.comparingByValue(Comparator.reverseOrder()));
    }

    private Optional<StudentStatsObject> bestRatedTeacher(Map<String, Double> availableTeacherRatings) {
        return teacherByComparator(availableTeacherRatings, "msg.studentstats.bestratedteacher.title",
                "msg.studentstats.bestratedteacher.subtitle", Map.Entry.comparingByValue());
    }

    private Optional<StudentStatsObject> teacherByComparator(Map<String, Double> availableTeacherRatings,
                                                             String titleMessage, String subtitleMessage,
                                                             Comparator<Map.Entry<String, Double>> maxComparator) {
        var selectedTeacher = availableTeacherRatings.entrySet()
                .stream()
                .max(maxComparator);
        return selectedTeacher.map(teacherWithRating -> StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageWithArgsFromContext(titleMessage, teacherWithRating.getKey()))
                        .subtitle(msgService.getMessageFromContext(subtitleMessage))
                        .value(teacherWithRating.getValue().toString())
                        .build())
                .build());
    }

    private Map<String, Double> getAvailableTeacherRatings(List<UsosStudies> usosStudies) {
        Map<String, Double> result = new HashMap<>();
        for (String teacherName : getUniqueTeachers(usosStudies)) {
            findReviewByTeacher(parseTeacherFullNameFromUsos(teacherName))
                    // if we don't have results based on full name, we will falback to last name
                    .or(() -> findReviewByTeacher(parseTeacherLastNameFromUsos(teacherName)))
                    .ifPresent(value -> result.put(teacherName, value));
        }
        return result;
    }

    private String parseTeacherFullNameFromUsos(String teacherName) {
        Matcher matcher = USOS_TEACHER_FULL_NAME_PATTERN.matcher(teacherName);
        return matcher.find() ? matcher.group().strip() : teacherName;
    }

    private String parseTeacherLastNameFromUsos(String teacherName) {
        Matcher matcher = USOS_TEACHER_LAST_NAME_PATTERN.matcher(teacherName);
        return matcher.find() ? matcher.group().strip() : teacherName;
    }

    private Optional<Double> findReviewByTeacher(String teacher) {
        return forumService.findFirstByFullNameContaining(teacher)
                .map(result -> result.getAverageRating().doubleValue());
    }

    @NotNull
    private Set<String> getUniqueTeachers(List<UsosStudies> usosStudies) {
        return usosStudies.stream()
                .map(UsosStudies::semesters)
                .flatMap(Collection::stream)
                .map(UsosSemester::courses)
                .flatMap(Collection::stream)
                .map(UsosCourse::getTeacher)
                .collect(Collectors.toSet());
    }


}
