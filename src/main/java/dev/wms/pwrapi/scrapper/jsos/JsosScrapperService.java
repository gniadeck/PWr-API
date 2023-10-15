package dev.wms.pwrapi.scrapper.jsos;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import dev.wms.pwrapi.dao.auth.AuthDao;
import dev.wms.pwrapi.utils.http.HttpClient;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import dev.wms.pwrapi.entity.jsos.weeks.JsosDaySubject;
import dev.wms.pwrapi.entity.jsos.weeks.JsosWeek;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import dev.wms.pwrapi.utils.jsos.JsosLessonsUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JsosScrapperService {

    private final AuthDao jsosAuthDao;

    /**
     * Return offseted weeks lessons. Uses JsosLessonUtils class for determining URL
     * @param login Login for JSOS
     * @param password Password for JSOS
     * @param offset
     * @return
     * @throws IOException
     * @throws LoginException
     */
    public JsosWeek getOffsetWeekLessons(String login, String password, int offset) throws IOException, LoginException{

        HttpClient client = jsosAuthDao.login(login, password);

        Document page = client.getDocument("https://jsos.pwr.edu.pl/index.php/student/zajecia/tydzien");

        String url;

        if(page.getElementsByClass("rozkladyZajecDzien rozkladyDzisiaj").size() == 0)
            offset--;

        if(offset < 0){
            offset = Math.abs(offset);
            url = JsosLessonsUtils.getUrlOfPreviousWeek(page);

            page = client.getDocument(url);

            return processUrlAndGetLessons(page);
        } else {

            url = JsosLessonsUtils.getUrlOfNextWeek(page, new HttpClient(), -1);
            page = client.getDocument(url);
            return processUrlAndGetLessons(page);

        }

    }

    private JsosWeek processUrlAndGetLessons(Document page){

        JsosWeek result = new JsosWeek();

        List<Element> coursesDays = page.getElementsByClass("wyzwalany");
        String weekTitle = page.getElementsByClass("rozklady_godzina").get(0).text();
        result.setWeekName(weekTitle.replace("<", "").replace(">", "").strip());

        LocalDate beginingWeekDate = LocalDate.parse(result.getWeekName().split(" ")[0]);


        for(Element course : coursesDays){

            String allText = course.text();

                String paragraph = course.getElementsByTag("p").get(0).text();
                String teacher = course.getElementsByClass("prow").get(0).text();

                JsosDaySubject toAdd = JsosDaySubject.builder()
                .data(allText.split(paragraph)[0].strip())
                .lokalizacja(allText.split(paragraph)[1].split(teacher)[0].strip())
                .prowadzacy(teacher)
                .nazwaPrzedmiotu(paragraph)
                .kodGrupy(allText.split(teacher)[1].replace("Kod grupy: ", "").strip().split(" ")[0].strip())
                .liczbaZapisanych(allText.split(teacher)[1].replace("Kod grupy: ", "").strip().replace("Liczba zapisanych: ", "").split(" ")[1])
                .type(JsosLessonsUtils.determineKindFromClassName(course.className()))
                .build();

            switch(allText.split(paragraph)[0].strip().split(",")[0].strip()){

                case "Poniedziałek":{
                    result.getPn().getSubjects().add(toAdd);
                    break;
                }
                case "Wtorek":{
                    result.getWt().getSubjects().add(toAdd);
                    break;
                }
                case "Środa":{
                    result.getSr().getSubjects().add(toAdd);
                    break;
                }
                case "Czwartek":{
                    result.getCzw().getSubjects().add(toAdd);
                    break;
                }
                case "Piątek":{
                    result.getPt().getSubjects().add(toAdd);
                    break;
                }
                case "Sobota":{
                    result.getSb().getSubjects().add(toAdd);
                    break;
                } case "Niedziela":{
                    result.getNd().getSubjects().add(toAdd);
                    break;
                }

            }

        }

        result.getPn().setDate(beginingWeekDate.toString());
        result.getWt().setDate(beginingWeekDate.plusDays(1).toString());
        result.getSr().setDate(beginingWeekDate.plusDays(2).toString());
        result.getCzw().setDate(beginingWeekDate.plusDays(3).toString());
        result.getPt().setDate(beginingWeekDate.plusDays(4).toString());
        result.getSb().setDate(beginingWeekDate.plusDays(5).toString());
        result.getNd().setDate(beginingWeekDate.plusDays(6).toString());
        
        return result;

    }

}
