package dev.wms.pwrapi.service.studentStats.cards.usos;

import dev.wms.pwrapi.domain.studentstats.StudentStatsCategory;
import dev.wms.pwrapi.dto.usos.UsosStudies;
import dev.wms.pwrapi.model.studentStats.StudentStatsObject;
import dev.wms.pwrapi.model.studentStats.StudentStatsText;
import dev.wms.pwrapi.service.internationalization.LocalizedMessageService;
import dev.wms.pwrapi.service.studentStats.errors.StudentStatsErrorReporter;
import dev.wms.pwrapi.utils.common.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UsosTimeCardCreator implements UsosCardCreator {

    private static final Pattern SEMESTER_BEGINNING_YEAR_PATTERN = Pattern.compile("\\d*\\/");
    private final StudentStatsErrorReporter errorReporter;
    private final LocalizedMessageService msgService;

    @Override
    public List<StudentStatsObject> getAlwaysPresentCards(List<UsosStudies> usosStudies) {
        return List.of(getFirstDayOfStudiesCard(usosStudies), getTotalTimeAtUniversityCard(usosStudies));
    }

    private StudentStatsObject getFirstDayOfStudiesCard(List<UsosStudies> usosStudies) {
        var result = getDateOfFirstDayOfStudies(usosStudies);
        return StudentStatsText.asObject(StudentStatsCategory.UNIVERSITY_STAFF,
                msgService.getMessageWithArgsFromContext("msg.UsosTimeCardCreator.first-day"), null,
                result);
    }

    private StudentStatsObject getTotalTimeAtUniversityCard(List<UsosStudies> usosStudies) {
        var result = getTotalTimeAtUniversity(usosStudies);
        return StudentStatsText.asObject(StudentStatsCategory.UNIVERSITY_STAFF,
                msgService.getMessageWithArgsFromContext("msg.UsosTimeCardCreator.time-from-first-day"),
                msgService.getMessageWithArgsFromContext("msg.UsosTimeCardCreator.time-from-first-day.description", getDateOfFirstDayOfStudies(usosStudies)),
                msgService.getMessageWithArgsFromContext("msg.UsosTimeCardCreator.time-from-first-day.unit", result.toString()));
    }

    private String getDateOfFirstDayOfStudies(List<UsosStudies> usosStudies){
        var firstDayOfStudies = getFirstDayOfStudiesDate(usosStudies);
        return DateUtils.formatToLocalizedDate(
                        firstDayOfStudies,
                        FormatStyle.LONG,
                        msgService.getLanguageFromContextOrDefault().getLocale()
                );
    }

    private Long getTotalTimeAtUniversity(List<UsosStudies> usosStudies){
        var firstDayOfStudies = getFirstDayOfStudiesDate(usosStudies);
        return Duration.between(
                                firstDayOfStudies.atStartOfDay(),
                                LocalDate.now().atStartOfDay())
                        .toDays();
    }

    private LocalDate getFirstDayOfStudiesDate(List<UsosStudies> usosStudies) {
        return usosStudies.stream()
                .map(UsosStudies::semesters)
                .flatMap(Collection::stream)
                .min(Comparator.comparingInt(semester -> getSemesterBeginningYear(semester.name())))
                .map(semester -> getFirstMondayOfOctoberAtYear(getSemesterBeginningYear(semester.name())))
                .orElseGet(() -> errorReporter.reportAndGetUndefinedDate("Error while creating card for first day of studies. " +
                        "Couldn't parse studies " + usosStudies));
    }

    private int getSemesterBeginningYear(String semesterName){
        Matcher matcher = SEMESTER_BEGINNING_YEAR_PATTERN.matcher(semesterName);
        matcher.find();
        return Integer.parseInt(matcher.group().replace("/",""));
    }

    private LocalDate getFirstMondayOfOctoberAtYear(int year){
        return LocalDate.of(year, Month.OCTOBER, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
    }
}
