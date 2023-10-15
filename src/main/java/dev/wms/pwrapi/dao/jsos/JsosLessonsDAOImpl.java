package dev.wms.pwrapi.dao.jsos;

import java.io.IOException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import dev.wms.pwrapi.dao.auth.AuthDao;
import dev.wms.pwrapi.utils.http.HttpClient;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.*;
import org.springframework.stereotype.Repository;

import dev.wms.pwrapi.scrapper.jsos.JsosScrapperService;
import dev.wms.pwrapi.entity.jsos.JsosLesson;
import dev.wms.pwrapi.entity.jsos.weeks.JsosDay;
import dev.wms.pwrapi.entity.jsos.weeks.JsosDaySubject;
import dev.wms.pwrapi.entity.jsos.weeks.JsosWeek;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import dev.wms.pwrapi.utils.jsos.JsosLessonsUtils;
import dev.wms.pwrapi.utils.jsos.exceptions.NoTodayClassException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Repository
@RequiredArgsConstructor
public class JsosLessonsDAOImpl implements JsosLessonsDAO {

    private final AuthDao jsosAuthDao;
    private final JsosScrapperService jsosScrapperService;

    @Override
    public JsosDay getTodaysLessons(String login, String password) throws LoginException, NoTodayClassException{

        HttpClient client = jsosAuthDao.login(login, password);

        Document page = client.getDocument("https://jsos.pwr.edu.pl/index.php/student/zajecia/tydzien");

        if(page.getElementsByClass("rozkladyZajecDzien rozkladyDzisiaj").size() == 0){
            throw new NoTodayClassException();
        }

        Element todayRow = page.getElementsByClass("rozkladyZajecDzien rozkladyDzisiaj").get(0);


        String todayNameDayShortcut = todayRow.getElementsByClass("rozkladyZajecDzienTekst").text().split(" ")[0];

        List<Element> coursesDays = page.getElementsByClass("wyzwalany");

        Map<String, String> dayNamesMap = Map.of("pn", "Poniedziałek", "wt", "Wtorek", "śr", "Środa", "cz", "Czwartek", "pt", "Piątek", "so", "Sobota", "n", "Niedziela");

        String fullDayName = dayNamesMap.get(todayNameDayShortcut);

        JsosDay result = new JsosDay();
        result.setDate(todayRow.text());


        return processDay(coursesDays, fullDayName, result);
    }

    @Override
    public JsosDay getTomorrowLessons(String login, String password) throws LoginException{

        HttpClient client = jsosAuthDao.login(login, password);


        Document page = client.getDocument("https://jsos.pwr.edu.pl/index.php/student/zajecia/tydzien");

        String todayNameDayShortcut;
        Element todayRow;
        String todayDate = "";
        if(page.getElementsByClass("rozkladyZajecDzien rozkladyDzisiaj").size() != 0){
        todayRow = page.getElementsByClass("rozkladyZajecDzien rozkladyDzisiaj").get(0);


        todayNameDayShortcut = todayRow.getElementsByClass("rozkladyZajecDzienTekst").text().split(" ")[0];
        } else {
            todayNameDayShortcut = "n";
            todayRow = page.getElementsByClass("rozkladyZajecDzienTekst").get(0);
        } 
        todayDate = todayRow.getElementsByTag("span").get(0).text();

        List<Element> coursesDays = page.getElementsByClass("wyzwalany");

        //determine element num from day
        List<String> dayNames = new ArrayList<String>(Arrays.asList("pn", "wt", "śr", "cz", "pt", "so", "n"));
        Map<String, String> dayNamesMap = Map.of("pn", "Poniedziałek", "wt", "Wtorek", "śr",
                "Środa", "cz", "Czwartek", "pt", "Piątek", "so", "Sobota", "n", "Niedziela");

        //check if its sunday
        int dayIndex = dayNames.indexOf(todayNameDayShortcut);
        if(todayNameDayShortcut.equalsIgnoreCase("n")) {
            dayIndex = 0;
        } else {
            dayIndex++;
        }
        
        String fullDayName = dayNamesMap.get(dayNames.get(dayIndex));

        JsosDay result = new JsosDay();

        // we could use only MonthDay class, but in order to do the day incrementation we need to provide a year (28-29 february case)
        MonthDay newDateMD = MonthDay.parse(todayDate, DateTimeFormatter.ofPattern("dd.MM"));
        LocalDate newDate = LocalDate.of(LocalDate.now().getYear(), newDateMD.getMonthValue(), newDateMD.getDayOfMonth());
        newDate = newDate.plusDays(1);
        result.setDate(dayNames.get(dayIndex) +" " +  newDate.format(DateTimeFormatter.ofPattern("dd.MM")));


        return processDay(coursesDays, fullDayName, result);

    }

    @NotNull
    private JsosDay processDay(List<Element> coursesDays, String fullDayName, JsosDay result) {
        List<JsosDaySubject> lessons = new ArrayList<JsosDaySubject>();

        for(Element course : coursesDays){

            String allText = course.text();
            if(allText.contains(fullDayName)){

                String paragraph = course.getElementsByTag("p").get(0).text();
                String teacher = course.getElementsByClass("prow").get(0).text();

                JsosDaySubject toAdd = new JsosDaySubject().builder()
                .data(allText.split(paragraph)[0].strip())
                .lokalizacja(allText.split(paragraph)[1].split(teacher)[0].strip())
                .prowadzacy(teacher)
                .nazwaPrzedmiotu(paragraph)
                .kodGrupy(allText.split(teacher)[1].replace("Kod grupy: ", "").strip().split(" ")[0].strip())
                .liczbaZapisanych(allText.split(teacher)[1].replace("Kod grupy: ", "").strip().replace("Liczba zapisanych: ", "").split(" ")[1])
                .type(JsosLessonsUtils.determineKindFromClassName(course.className()))
                .build();

                lessons.add(toAdd);

            }

        }

        result.setSubjects(lessons);

        return result;
    }

    @Override
    public JsosWeek getThisWeekLessons(String login, String password) throws IOException, LoginException{
        return jsosScrapperService.getOffsetWeekLessons(login, password, 0);
    }

    @Override
    public JsosWeek getNextWeekLessons(String login, String password) throws IOException, LoginException{
        return jsosScrapperService.getOffsetWeekLessons(login, password, 1);
    }

    @Override
    public JsosWeek getOffsetWeekLessons(String login, String password, int offset) throws IOException, LoginException{
        return jsosScrapperService.getOffsetWeekLessons(login, password, offset);
    }


    @Override
    public List<JsosLesson> getAllLessons(String login, String password) throws IOException, LoginException{

        HttpClient client = jsosAuthDao.login(login, password);

        Request request = new Request.Builder()
            .url("https://jsos.pwr.edu.pl/index.php/student/zajecia")
            .build();

        client.getResponse(request);

        Document page = client.getDocument("https://jsos.pwr.edu.pl/index.php/student/zajecia");

        List<Element> rows = page.getElementsByClass("kliknij");
        

        List<JsosLesson> result = new ArrayList<JsosLesson>();

        for(Element row : rows){

            List<Element> cells = row.getElementsByTag("td");

            JsosLesson toAdd = JsosLesson.builder()
                .nazwaKursu(cells.get(0).text().split(" ", 2)[1])
                .idKursu(cells.get(0).text().split(" ")[0])
                .prowadzacy(cells.get(1).text())
                .kodGrupy(cells.get(2).text())
                .termin(cells.get(3).text())
                .godziny(cells.get(4).text())
                .ects(cells.get(5).text())
                .build();

            result.add(toAdd);

        }

        return result;
    }
    
    
}
