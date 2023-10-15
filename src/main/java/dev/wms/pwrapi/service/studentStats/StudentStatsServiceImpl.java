package dev.wms.pwrapi.service.studentStats;

import dev.wms.pwrapi.domain.studentstats.StudentStatsCategory;
import dev.wms.pwrapi.dto.usos.UsosStudentStatus;
import dev.wms.pwrapi.model.studentStats.*;
import dev.wms.pwrapi.service.internationalization.LocalizedMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class StudentStatsServiceImpl implements StudentStatsService {

    private final List<StudentStatsDataService> dataServices;
    private final StudentStatsPersonalDataService personalDataService;
    private final LocalizedMessageService msgService;

    @Override
    public StudentStatsData get(String login, String password) {
        return StudentStatsData.builder()
                .personalData(personalDataService.getPersonalData(login, password))
                .content(dataServices.stream()
                        .map(service -> service.getData(login, password, LocaleContextHolder.getLocaleContext()))
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .toList())
                .build();
    }

    @Override
    public StudentStatsData getStaticMockedData() {
        SecureRandom random = new SecureRandom();

        StudentStatsPersonalData mockedPersonalData = StudentStatsPersonalData.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .currentFaculty("W04n")
                .currentMajor("IST")
                .semester(4)
                .currentStageOfStudies(1)
                .studentStatus(UsosStudentStatus.ACTIVE_STUDENT)
                .phdStudentStatus(null)
                .studiesType("")
                .indexNumber(123456)
                .build();

        ArrayList<StudentStatsObject> studentStatsMockContent = new ArrayList<>();

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PAYMENTS)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.sum-of-money"))
                        .subtitle("")
                        .value("2137zl")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsDoubleText.builder()
                        .title(msgService.getMessageFromContext("msg.avg-grades-weighted-not-weighted.all"))
                        .subtitle("test")
                        .value1("4.20")
                        .value2("4.2137")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.percentile.all"))
                        .subtitle("")
                        .value("99")
                        .build())
                .build());


        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsDoubleText.builder()
                        .title(msgService.getMessageFromContext("msg.avg-grades-weighted-not-weighted.last-sem"))
                        .subtitle("test")
                        .value1("4.19")
                        .value2("2.137")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.percentile.last-sem"))
                        .subtitle("")
                        .value("98")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.percentile.last-sem"))
                        .subtitle("")
                        .value("98")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.avg-as-graph"))
                        .chartType(StudentStatsChartType.BAR)
                        .subtitle("")
                        .values(List.of(new StudentStatsChartValue(4.15, "sem 1"),
                                new StudentStatsChartValue(4.80, "sem 2"),
                                new StudentStatsChartValue(4.10, "sem 3"),
                                new StudentStatsChartValue(3.55, "sem 4")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_SEMESTER)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.ects.sem"))
                        .subtitle("")
                        .value("30")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_SEMESTER)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.days-passed-percent"))
                        .subtitle("")
                        .value("33%")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_SEMESTER)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.days-until-exam"))
                        .subtitle(msgService.getMessageFromContext("msg.StudentStatsService.days-until-exam-with-weekends"))
                        .value("79")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.ects-balance"))
                        .subtitle("")
                        .value("90")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.studies-completion-percent"))
                        .subtitle("")
                        .value("15%")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.messages-count"))
                        .subtitle(msgService.getMessageFromContext("msg.StudentStatsService.only-for-jsos"))
                        .value("69")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.days-at-pwr"))
                        .subtitle(msgService.getMessageFromContext("msg.StudentStatsService.lecture-assumption"))
                        .value("2137")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.semesters-of-wf"))
                        .subtitle("")
                        .value("1")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.semesters-of-lang"))
                        .subtitle("")
                        .value("0")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.semesters-of-humanities"))
                        .subtitle("")
                        .value("1")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.SCHOLARSHIPS)
                .content(StudentStatsFlag.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.has-scholarship"))
                        .value(true)
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.SCHOLARSHIPS)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.semesters-with-scholarship"))
                        .subtitle("")
                        .value("2")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.SCHOLARSHIPS)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.money-from-scholarships"))
                        .subtitle("")
                        .value("42069zl")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.SCHOLARSHIPS)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.graph-of-scholarship-thresholds"))
                        .subtitle("")
                        .chartType(StudentStatsChartType.LINE)
                        .values(List.of(new StudentStatsChartValue(4.80, "semestr letni 2021/2022"),
                                new StudentStatsChartValue(4.78, "semestr zimowy 2021/2022"),
                                new StudentStatsChartValue(4.90, "semestr letni 2022/2023"),
                                new StudentStatsChartValue(5.00, "semestr zimowy 2022/2023")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.SCHOLARSHIPS)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.num-of-fields-of-studies-with-scholarship"))
                        .subtitle("")
                        .value("69/420")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.best-grading-teacher"))
                        .subtitle("Witold Jacak")
                        .chartType(StudentStatsChartType.LINE)
                        .values(List.of(new StudentStatsChartValue(0.0, "2.0"),
                                new StudentStatsChartValue(0.0, "3.0"),
                                new StudentStatsChartValue(0.0, "3.5"),
                                new StudentStatsChartValue(15.0, "4.0"),
                                new StudentStatsChartValue(20.0, "4.5"),
                                new StudentStatsChartValue(40.0, "5.0"),
                                new StudentStatsChartValue(25.0, "5.5")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.worst-grading-teacher"))
                        .subtitle("Jezy sas")
                        .chartType(StudentStatsChartType.LINE)
                        .values(List.of(new StudentStatsChartValue(50.0, "2.0"),
                                new StudentStatsChartValue(15.0, "3.0"),
                                new StudentStatsChartValue(15.0, "3.5"),
                                new StudentStatsChartValue(10.0, "4.0"),
                                new StudentStatsChartValue(5.0, "4.5"),
                                new StudentStatsChartValue(3.0, "5.0"),
                                new StudentStatsChartValue(2.0, "5.5")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.best-polwro-teacher"))
                        .subtitle("Witold Jacak")
                        .chartType(StudentStatsChartType.LINE)
                        .values(List.of(new StudentStatsChartValue(0.0, "2.0"),
                                new StudentStatsChartValue(0.0, "3.0"),
                                new StudentStatsChartValue(0.0, "3.5"),
                                new StudentStatsChartValue(0.0, "4.0"),
                                new StudentStatsChartValue(0.0, "4.5"),
                                new StudentStatsChartValue(60.0, "5.0"),
                                new StudentStatsChartValue(40.0, "5.5")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.worst-polwro-teacher"))
                        .subtitle("Jezy sas")
                        .chartType(StudentStatsChartType.LINE)
                        .values(List.of(new StudentStatsChartValue(50.0, "2.0"),
                                new StudentStatsChartValue(20.0, "3.0"),
                                new StudentStatsChartValue(10.0, "3.5"),
                                new StudentStatsChartValue(10.0, "4.0"),
                                new StudentStatsChartValue(10.0, "4.5"),
                                new StudentStatsChartValue(0.0, "5.0"),
                                new StudentStatsChartValue(0.0, "5.5")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.avg-as-graph"))
                        .chartType(StudentStatsChartType.PIE)
                        .subtitle("")
                        .values(List.of(new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "sem 1"),
                                new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "sem 2"),
                                new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "sem 3"),
                                new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "sem 4")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.worst-grading-teacher"))
                        .subtitle("Jezy sas")
                        .chartType(StudentStatsChartType.PIE)
                        .values(List.of(new StudentStatsChartValue(50.0, "2.0"),
                                new StudentStatsChartValue(15.0, "3.0"),
                                new StudentStatsChartValue(15.0, "3.5"),
                                new StudentStatsChartValue(10.0, "4.0"),
                                new StudentStatsChartValue(5.0, "4.5"),
                                new StudentStatsChartValue(3.0, "5.0"),
                                new StudentStatsChartValue(2.0, "5.5")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.LIBRARY)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.number-of-borrowed-books"))
                        .subtitle("")
                        .value("123")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.LIBRARY)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.sum-of-fines-for-books"))
                        .subtitle("")
                        .value("21.37zl")
                        .build())
                .build());

        return StudentStatsData.builder()
                .personalData(mockedPersonalData)
                .content(studentStatsMockContent)
                .build();
    }


    @Override
    public StudentStatsData getDynamicMockedData(Integer numOfBlocks) {
        SecureRandom random = new SecureRandom();

        ArrayList<String> firstNames = new ArrayList<>(Arrays.asList("Adam", "Barbara", "Cezary", "Dorota", "Edward", "Filip",
                "Gosia", "Henryk", "Igor", "Jan"));
        List<String> lastNames = Arrays.asList("Kowalski", "Nowak", "Wójcik", "Lewandowski", "Kamiński", "Dąbrowski",
                "Zieliński", "Szymański", "Woźniak", "Kozłowski");

        StudentStatsPersonalData mockedPersonalData = StudentStatsPersonalData.builder()
                .firstName(firstNames.get(random.nextInt(firstNames.size())))
                .lastName(lastNames.get(random.nextInt(lastNames.size())))
                .currentFaculty("W04n")
                .currentMajor("IST")
                .semester(random.nextInt(7) + 1)
                .currentStageOfStudies(random.nextInt(2) + 1)
                .studentStatus(UsosStudentStatus.ACTIVE_STUDENT)
                .indexNumber(123456)
                .build();

        ArrayList<StudentStatsObject> studentStatsMockContent = new ArrayList<>();

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PAYMENTS)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.sum-of-money"))
                        .subtitle("")
                        .value(String.format("%.2f", (random.nextDouble() * 1000)))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsDoubleText.builder()
                        .title(msgService.getMessageFromContext("msg.avg-grades-weighted-not-weighted.all"))
                        .subtitle("test")
                        .value1(String.format("%.2f", (random.nextDouble() + 4)))
                        .value2(String.format("%.2f", (random.nextDouble() + 4)))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.percentile.all"))
                        .subtitle("")
                        .value(String.valueOf(random.nextInt(101)))
                        .build())
                .build());


        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsDoubleText.builder()
                        .title(msgService.getMessageFromContext("msg.avg-grades-weighted-not-weighted.last-sem"))
                        .subtitle("test")
                        .value1(String.format("%.2f", (random.nextDouble() + 4)))
                        .value2(String.format("%.2f", (random.nextDouble() + 4)))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.percentile.last-sem"))
                        .subtitle("")
                        .value(String.valueOf(random.nextInt(101)))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.percentile.last-sem"))
                        .subtitle("")
                        .value(String.valueOf(random.nextInt(101)))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.avg-as-graph"))
                        .chartType(StudentStatsChartType.LINE)
                        .subtitle("")
                        .values(List.of(new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "sem 1"),
                                new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "sem 2"),
                                new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "sem 3"),
                                new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "sem 4")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.GPA)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.avg-as-graph"))
                        .chartType(StudentStatsChartType.PIE)
                        .subtitle("")
                        .values(List.of(new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "sem 1"),
                                new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "sem 2"),
                                new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "sem 3"),
                                new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "sem 4")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_SEMESTER)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.ects.sem"))
                        .subtitle("")
                        .value(String.valueOf(random.nextInt(5)+26))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_SEMESTER)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.days-passed-percent"))
                        .subtitle("")
                        .value((random.nextInt(81))  + "%")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_SEMESTER)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.days-until-exam"))
                        .subtitle(msgService.getMessageFromContext("msg.StudentStatsService.days-until-exam-with-weekends"))
                        .value(String.valueOf(random.nextInt(100)))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.ects-balance"))
                        .subtitle("")
                        .value(String.valueOf(random.nextInt(250)))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.studies-completion-percent"))
                        .subtitle("")
                        .value((random.nextInt(91)) + "%")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.messages-count"))
                        .subtitle(msgService.getMessageFromContext("msg.StudentStatsService.only-for-jsos"))
                        .value(String.valueOf(random.nextInt(5)+26))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.days-at-pwr"))
                        .subtitle(msgService.getMessageFromContext("msg.StudentStatsService.lecture-assumption"))
                        .value(String.valueOf(random.nextInt(1000)+500))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.semesters-of-wf"))
                        .subtitle("")
                        .value(String.valueOf(random.nextInt(3)))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.semesters-of-lang"))
                        .subtitle("")
                        .value(String.valueOf(random.nextInt(3)))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.PROGRESS_OF_STUDIES)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.semesters-of-humanities"))
                        .subtitle("")
                        .value(String.valueOf(random.nextInt(2)))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.SCHOLARSHIPS)
                .content(StudentStatsFlag.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.has-scholarship"))
                        .value(random.nextBoolean())
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.SCHOLARSHIPS)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.semesters-with-scholarship"))
                        .subtitle("")
                        .value(String.valueOf(random.nextInt(5)))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.SCHOLARSHIPS)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.money-from-scholarships"))
                        .subtitle("")
                        .value((random.nextInt(10000)) + "zl")
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.SCHOLARSHIPS)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.graph-of-scholarship-thresholds"))
                        .subtitle("")
                        .chartType(StudentStatsChartType.LINE)
                        .values(List.of(new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "semestr letni 2021/2022"),
                                new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "semestr zimowy 2021/2022"),
                                new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "semestr letni 2022/2023"),
                                new StudentStatsChartValue(Math.round(random.nextDouble()*100 + 400)/100.0, "semestr zimowy 2022/2023")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.SCHOLARSHIPS)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.num-of-fields-of-studies-with-scholarship"))
                        .subtitle("")
                        .value((random.nextInt(100)) + "/" + (random.nextInt(50)+100))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.best-grading-teacher"))
                        .subtitle("Witold Jacak")
                        .chartType(StudentStatsChartType.LINE)
                        .values(List.of(new StudentStatsChartValue(0.0, "2.0"),
                                new StudentStatsChartValue(0.0, "3.0"),
                                new StudentStatsChartValue(0.0, "3.5"),
                                new StudentStatsChartValue(15.0, "4.0"),
                                new StudentStatsChartValue(20.0, "4.5"),
                                new StudentStatsChartValue(40.0, "5.0"),
                                new StudentStatsChartValue(25.0, "5.5")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.worst-grading-teacher"))
                        .subtitle("Jezy sas")
                        .chartType(StudentStatsChartType.LINE)
                        .values(List.of(new StudentStatsChartValue(50.0, "2.0"),
                                new StudentStatsChartValue(15.0, "3.0"),
                                new StudentStatsChartValue(15.0, "3.5"),
                                new StudentStatsChartValue(10.0, "4.0"),
                                new StudentStatsChartValue(5.0, "4.5"),
                                new StudentStatsChartValue(3.0, "5.0"),
                                new StudentStatsChartValue(2.0, "5.5")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.worst-grading-teacher"))
                        .subtitle("Jezy sas")
                        .chartType(StudentStatsChartType.PIE)
                        .values(List.of(new StudentStatsChartValue(50.0, "2.0"),
                                new StudentStatsChartValue(15.0, "3.0"),
                                new StudentStatsChartValue(15.0, "3.5"),
                                new StudentStatsChartValue(10.0, "4.0"),
                                new StudentStatsChartValue(5.0, "4.5"),
                                new StudentStatsChartValue(3.0, "5.0"),
                                new StudentStatsChartValue(2.0, "5.5")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.best-polwro-teacher"))
                        .subtitle("Witold Jacak")
                        .chartType(StudentStatsChartType.LINE)
                        .values(List.of(new StudentStatsChartValue(0.0, "2.0"),
                                new StudentStatsChartValue(0.0, "3.0"),
                                new StudentStatsChartValue(0.0, "3.5"),
                                new StudentStatsChartValue(0.0, "4.0"),
                                new StudentStatsChartValue(0.0, "4.5"),
                                new StudentStatsChartValue(60.0, "5.0"),
                                new StudentStatsChartValue(40.0, "5.5")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.UNIVERSITY_STAFF)
                .content(StudentStatsChart.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.worst-polwro-teacher"))
                        .subtitle("Jezy sas")
                        .chartType(StudentStatsChartType.LINE)
                        .values(List.of(new StudentStatsChartValue(50.0, "2.0"),
                                new StudentStatsChartValue(20.0, "3.0"),
                                new StudentStatsChartValue(10.0, "3.5"),
                                new StudentStatsChartValue(10.0, "4.0"),
                                new StudentStatsChartValue(10.0, "4.5"),
                                new StudentStatsChartValue(0.0, "5.0"),
                                new StudentStatsChartValue(0.0, "5.5")))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.LIBRARY)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.number-of-borrowed-books"))
                        .subtitle("")
                        .value(String.valueOf(random.nextInt(100)))
                        .build())
                .build());

        studentStatsMockContent.add(StudentStatsObject.builder()
                .category(StudentStatsCategory.LIBRARY)
                .content(StudentStatsText.builder()
                        .title(msgService.getMessageFromContext("msg.StudentStatsService.sum-of-fines-for-books"))
                        .subtitle("")
                        .value(String.format("%.2f", (random.nextDouble()*100)) + "zl")
                        .build())
                .build());

        return StudentStatsData.builder()
                .personalData(mockedPersonalData)
                .content(getRandomElements(studentStatsMockContent, numOfBlocks))
                .build();
    }

    private <T> List<T> getRandomElements(List<T> list, Integer numOfItems) {
        List<T> randomElements = new ArrayList<>();
        if(numOfItems <= list.size()) {
            Collections.shuffle(list);
            for (int i = 0; i < numOfItems; i++) {
                randomElements.add(list.get(i));
            }
        } else {
            for (int i = 0; i < numOfItems; i++) {
                if(i%28 == 0) {
                    Collections.shuffle(list);
                }
                randomElements.add(list.get(i%28));
            }
        }
        return randomElements;
    }
}