package dev.wms.pwrapi.scrapper.jsos;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dev.wms.pwrapi.dao.jsos.JsosGeneralDAOImpl;
import dev.wms.pwrapi.utils.http.HttpUtils;
import dev.wms.pwrapi.utils.jsos.JsosHttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import dev.wms.pwrapi.dto.jsos.JsosConnection;
import dev.wms.pwrapi.entity.jsos.weeks.JsosDaySubject;
import dev.wms.pwrapi.entity.jsos.weeks.JsosWeek;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import dev.wms.pwrapi.utils.jsos.JsosLessonsUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JsosScrapperServices {


    /**
     * Return offseted weeks lessons. Uses JsosLessonUtils class for determining URL
     * @param login Login for JSOS
     * @param password Password for JSOS
     * @param offset
     * @return
     * @throws IOException
     * @throws LoginException
     */
    public static JsosWeek getOffsetWeekLessons(String login, String password, int offset) throws IOException, LoginException{

        OkHttpClient client = JsosHttpUtils.getLoggedClient(login, password);

        Document page = HttpUtils.makeRequestWithClientAndGetDocument(client,
                "https://jsos.pwr.edu.pl/index.php/student/zajecia/tydzien");

        String url;

        if(page.getElementsByClass("rozkladyZajecDzien rozkladyDzisiaj").size() == 0)
            offset--;

        if(offset < 0){
            offset = Math.abs(offset);
            url = JsosLessonsUtils.getUrlOfPreviousWeek(page, client, offset);

            page = Jsoup.parse(client.newCall(new Request.Builder().url(url).build()).execute().body().string());

            return JsosScrapperServices.processUrlAndGetLessons(page);
        } else {

            url = JsosLessonsUtils.getUrlOfNextWeek(page, client, offset);
            page = Jsoup.parse(client.newCall(new Request.Builder().url(url).build()).execute().body().string());
            return JsosScrapperServices.processUrlAndGetLessons(page);

        }

    }


    public static JsosWeek getWeekLessons(String login, String password, String url) throws IOException, LoginException{

        JsosGeneralDAOImpl general = new JsosGeneralDAOImpl();
        JsosConnection connection = general.login(login, password);
        OkHttpClient client = general.getClient();
        JsosWeek result = new JsosWeek();

        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = client.newCall(request).execute();

        Document page = Jsoup.parse(response.body().string());

        return processUrlAndGetLessons(page);

    }

    private static JsosWeek processUrlAndGetLessons(Document page){

        JsosWeek result = new JsosWeek();

        System.out.println(page.html());

        List<Element> coursesDays = page.getElementsByClass("wyzwalany");
        System.out.println("coursesDays: " + coursesDays);
        String weekTitle = page.getElementsByClass("rozklady_godzina").get(0).text();
        result.setWeekName(weekTitle.replace("<", "").replace(">", "").strip());

        LocalDate beginingWeekDate = LocalDate.parse(result.getWeekName().split(" ")[0]);



        ArrayList<JsosDaySubject> lessons = new ArrayList<JsosDaySubject>();

        for(Element course : coursesDays){

            String allText = course.text();

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
                System.out.println("Detected " + toAdd);
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
