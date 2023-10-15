package dev.wms.pwrapi.service.studentStats.cards.usos;

import dev.wms.pwrapi.domain.studentstats.StudentStatsCategory;
import dev.wms.pwrapi.dto.usos.UsosCourse;
import dev.wms.pwrapi.dto.usos.UsosSemester;
import dev.wms.pwrapi.dto.usos.UsosStudies;
import dev.wms.pwrapi.model.studentStats.*;
import dev.wms.pwrapi.service.internationalization.LocalizedMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsosAveragesCardCreator implements UsosCardCreator {

    private final LocalizedMessageService msgService;

    @Override
    public List<Optional<StudentStatsObject>> getOptionalCards(List<UsosStudies> userStudies) {
        return List.of(createWeightedAndUnweightedAvgGradeFromWholeSemesterCard(userStudies),
                createWeightedAndUnweightedAvgGradeFromLAstSemesterCard(userStudies));
    }

    @Override
    public List<StudentStatsObject> getAlwaysPresentCards(List<UsosStudies> usosStudies) {
        return List.of(createUnweightedAvgGraphPerSemester(usosStudies), createWeightedAvgGraphPerSemester(usosStudies));
    }

    private StudentStatsObject createWeightedAvgGraphPerSemester(List<UsosStudies> usosStudies) {
        var values = usosStudies.stream()
                .flatMap(studies -> studies.semesters().stream())
                .map(semester -> StudentStatsChartValue.of(weightedAverageGradeFromOneSemester(semester).doubleValue(), semester.name()))
                .collect(Collectors.toList());
        Collections.reverse(values);
        return StudentStatsObject.builder()
                .content(StudentStatsChart.builder()
                        .values(values)
                        .chartType(StudentStatsChartType.LINE)
                        .title(msgService.getMessageFromContext("msg.weighted-average-per-semester-graph"))
                        .build())
                .category(StudentStatsCategory.GPA)
                .build();
    }

    private StudentStatsObject createUnweightedAvgGraphPerSemester(List<UsosStudies> usosStudies) {
        var values = usosStudies.stream()
                .flatMap(studies -> studies.semesters().stream())
                .map(semester -> StudentStatsChartValue.of(unweightedAverageGradeFromOneSemester(semester).doubleValue(), semester.name()))
                .collect(Collectors.toList());
        Collections.reverse(values);
        return StudentStatsObject.builder()
                .content(StudentStatsChart.builder()
                        .values(values)
                        .chartType(StudentStatsChartType.LINE)
                        .title(msgService.getMessageFromContext("msg.unweighted-average-per-semester-graph"))
                        .build())
                .category(StudentStatsCategory.GPA)
                .build();
    }

    private Optional<StudentStatsObject> createWeightedAndUnweightedAvgGradeFromWholeSemesterCard(
            List<UsosStudies> userStudies) {
        return Optional.of(StudentStatsDoubleText.asObject(StudentStatsCategory.GPA,
                msgService.getMessageFromContext("msg.avg-grades-weighted-not-weighted.all"), null,
                String.valueOf(weightedAverageGradeFromAllSemesters(userStudies)),
                String.valueOf(unweightedAverageGradeFromAllSemesters(userStudies))));
    }


    private Optional<StudentStatsObject> createWeightedAndUnweightedAvgGradeFromLAstSemesterCard(
            List<UsosStudies> userStudies) {
        return Optional.of(StudentStatsDoubleText.asObject(StudentStatsCategory.GPA,
                msgService.getMessageFromContext("msg.avg-grades-weighted-not-weighted.last-sem"), null,
                String.valueOf(weightedAverageGradeFromOneSemester(userStudies.get(0).lastSemester())),
                String.valueOf(unweightedAverageGradeFromOneSemester(userStudies.get(0).lastSemester()))));
    }

    private BigDecimal weightedAverageGradeFromAllSemesters(List<UsosStudies> userStudies) {
        BigDecimal marksSum = userStudies.stream()
                .map(UsosStudies::semesters)
                .flatMap(List::stream)
                .map(UsosSemester::courses)
                .flatMap(List::stream)
                .map(course -> course.getMark().multiply(BigDecimal.valueOf(course.getECTS())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int ECTSSum = userStudies.stream()
                .map(UsosStudies::semesters)
                .flatMap(List::stream)
                .map(UsosSemester::courses)
                .flatMap(List::stream)
                .mapToInt(UsosCourse::getECTS)
                .sum();

        return marksSum.divide(BigDecimal.valueOf(ECTSSum), 2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal unweightedAverageGradeFromAllSemesters(List<UsosStudies> userStudies) {
        BigDecimal marksSum = userStudies.stream()
                .map(UsosStudies::semesters)
                .flatMap(List::stream)
                .map(UsosSemester::courses)
                .flatMap(List::stream)
                .map(UsosCourse::getMark)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int marksCounter = userStudies.stream()
                .map(UsosStudies::semesters)
                .flatMap(List::stream)
                .map(UsosSemester::courses)
                .mapToInt(List::size)
                .sum();

        return marksSum.divide(BigDecimal.valueOf(marksCounter), 2, RoundingMode.HALF_EVEN);
    }


    private BigDecimal weightedAverageGradeFromOneSemester(UsosSemester semester) {
        BigDecimal marksSum = semester.courses().stream()
                .map(course -> course.getMark().multiply(BigDecimal.valueOf(course.getECTS())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double ECTSSum = semester.courses().stream()
                .mapToDouble(UsosCourse::getECTS)
                .sum();
        return marksSum.divide(BigDecimal.valueOf(ECTSSum), 3, RoundingMode.HALF_EVEN);
    }

    private BigDecimal unweightedAverageGradeFromOneSemester(UsosSemester semester) {
        int marksCounter = semester.courses().size();
        BigDecimal marksSum = semester.courses().stream()
                .map(UsosCourse::getMark)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return marksSum.divide(BigDecimal.valueOf(marksCounter), 3, RoundingMode.HALF_EVEN);
    }

}
