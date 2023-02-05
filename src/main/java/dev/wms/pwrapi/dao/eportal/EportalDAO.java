package dev.wms.pwrapi.dao.eportal;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.wms.pwrapi.entity.eportal.Mark;
import dev.wms.pwrapi.entity.eportal.MarkSummary;
import dev.wms.pwrapi.dto.eportal.sections.EportalSection;
import dev.wms.pwrapi.dto.eportal.sections.EportalSectionElement;
import dev.wms.pwrapi.utils.cookies.CookieJarImpl;
import dev.wms.pwrapi.utils.eportal.exceptions.WrongCourseIdException;
import dev.wms.pwrapi.utils.generalExceptions.LoginException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Repository;

import dev.wms.pwrapi.scrapper.eportal.EportalScrapperService;
import dev.wms.pwrapi.dto.eportal.courseTitle;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Repository
public class EportalDAO {

    public String login(String login, String password) throws IOException, LoginException {
        EportalScrapperService.loginToEportal(login, password);
        OkHttpClient client = EportalScrapperService.getClient();
        return client.toString();

    }

    public String getEportalKursy(String login, String password) throws IOException, LoginException {

        EportalScrapperService.loginToEportal(login, password);
        OkHttpClient client = EportalScrapperService.getClient();

        Request request = new Request.Builder()
                .url("https://eportal.pwr.edu.pl/my/")
                .build();
        Response response = client.newCall(request).execute();
        Document page = Jsoup.parse(response.body().string());

        List<Element> wydzialy = page.getElementsByClass("categoryname");
        List<Element> nazwyKursow = page.getElementsByClass("multiline");
        List<Element> detailsLinks = page.getElementsByClass("aalink");

        ArrayList<courseTitle> result = new ArrayList<courseTitle>();

        for (int i = 0; i < nazwyKursow.size(); i++) {

            courseTitle toAdd = new courseTitle().builder()
                    .wydzial(wydzialy.get(i).text())
                    .nazwa(nazwyKursow.get(i).text())
                    .detailsLink(detailsLinks.get(i).text())
                    .build();

            System.out.println("Adding " + result);
            result.add(toAdd);

        }

        String moodleSession = ((CookieJarImpl) client.cookieJar()).getCookieStore().get("eportal.pwr.edu.pl").get(0)
                .value();
        System.out.println(moodleSession);

        String sessKey = page.getElementsByAttributeValue("name", "sesskey").get(0).attr("value");

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\"index\":0,\"methodname\":\"core_course_get_enrolled_courses_by_timeline_classification\",\"args\":{\"offset\":0,\"limit\":0,\"classification\":\"all\",\"sort\":\"fullname\",\"customfieldname\":\"\",\"customfieldvalue\":\"\"}}]");
        request = new Request.Builder()
                .url("https://eportal.pwr.edu.pl/lib/ajax/service.php?sesskey=" + sessKey
                        + "&info=core_course_get_enrolled_courses_by_timeline_classification")
                .method("POST", body)
                .build();
        response = client.newCall(request).execute();

        return response.body().string();
    }

    public ArrayList<EportalSection> getEportalSekcje(String login, String password, int id) throws IOException, LoginException, WrongCourseIdException {

        EportalScrapperService.loginToEportal(login, password);
        OkHttpClient client = EportalScrapperService.getClient();


        Request request = new Request.Builder()
                .url("https://eportal.pwr.edu.pl/course/view.php?id=" + id)
                .method("GET", null)
                .addHeader("sec-ch-ua",
                        "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"100\", \"Google Chrome\";v=\"100\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36")
                .addHeader("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Sec-Fetch-Dest", "document")
                .build();

        Response response = client.newCall(request).execute();

        Document page = Jsoup.parse(response.body().string());
        System.out.println("Page title: " + page.title());
        if (page.title().contains("Błąd")) throw new WrongCourseIdException();

        ArrayList<EportalSection> result = new ArrayList<EportalSection>();

        List<Element> sections = page.getElementsByClass("content");

        List<Element> sectionsName = page.getElementsByClass("sectionname");
        System.out.println("Sections: " + sections.get(0) + " , " + sections.get(1));

        for (int i = 0; i < sectionsName.size(); i++) {

            Element section = sections.get(i);
            ArrayList<EportalSectionElement> sectionElements = new ArrayList<EportalSectionElement>();

            List<Element> sectionRealElements = section.getElementsByClass("activityinstance");
            System.out.println("SectionRealElements: " + sectionRealElements);
            for (int j = 0; j < sectionRealElements.size(); j++) {

                Element element = sectionRealElements.get(j);
                System.out.println("Analizying element " + element.text());
                String type = "";

                if (element.getElementsByClass("accesshide ").size() != 0)
                    type = element.getElementsByClass("accesshide ").get(0).text();
                EportalSectionElement ElementToAdd = new EportalSectionElement().builder()
                        .title(element.text().replace(type, ""))
                        .type(type)
                        .build();

                sectionElements.add(ElementToAdd);
            }

            EportalSection toAdd = new EportalSection().builder()
                    .sectionName(sectionsName.get(i).text())
                    .elements(sectionElements)
                    .build();

            result.add(toAdd);

        }

        return result;
    }

    public List<MarkSummary> getEportalOceny(String login, String password, int id) {

        try {
            EportalScrapperService.loginToEportal(login, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        OkHttpClient client = EportalScrapperService.getClient();

        Request request = new Request.Builder()
                .url("https://eportal.pwr.edu.pl/grade/report/user/index.php?id=" + id)
                .method("GET", null)
                .addHeader("sec-ch-ua",
                        "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"100\", \"Google Chrome\";v=\"100\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36")
                .addHeader("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Sec-Fetch-Dest", "document")
                .build();


        Response response = null;
        Document page;
        try {
            response = client.newCall(request).execute();
            page = Jsoup.parse(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Page title: " + page.title());
        if (page.title().contains("Błąd")) throw new WrongCourseIdException();

        List<Element> marksTable = page.getElementsByClass("user-grade");

        List<MarkSummary> result = new ArrayList<>();

        for (Element markTable : marksTable) {

            MarkSummary markSummaryFromTable = new MarkSummary();
            markSummaryFromTable.setCourseName(markTable.getElementsByClass("column-itemname").get(1).text());

            List<Element> rowsToVisit = markTable.getElementsByTag("tr");

            //remove first two rows as it contains headers
            rowsToVisit.remove(0);
            rowsToVisit.remove(0);

            for (Element row : rowsToVisit) {

                Mark toAdd = Mark.builder()
                        .name(row.getElementsByClass("column-itemname").first().text())
                        .weight(row.getElementsByClass("column-weight").first().text())
                        .value(row.getElementsByClass("column-grade").first().text())
                        .range(row.getElementsByClass("column-range").first().text())
                        .percentage(row.getElementsByClass("column-percentage").first().text())
                        .markName(row.getElementsByClass("column-lettergrade").first().text())
                        .feedback(row.getElementsByClass("column-feedback").first().text())
                        .courseParticipation(row.getElementsByClass("column-contributiontocoursetotal").first().text())
                        .build();

                markSummaryFromTable.getMarks().add(toAdd);
                System.out.println("Added " + toAdd);

            }
            result.add(markSummaryFromTable);
        }


        return result;
    }
}
