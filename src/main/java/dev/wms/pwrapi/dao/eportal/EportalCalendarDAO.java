package dev.wms.pwrapi.dao.eportal;


import dev.wms.pwrapi.entity.eportal.calendar.CalendarEvent;
import dev.wms.pwrapi.entity.eportal.calendar.CalendarMonth;
import dev.wms.pwrapi.scrapper.eportal.EportalScrapperService;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EportalCalendarDAO {

    public CalendarMonth getEventsWithOffset(String login, String password, int offset) throws IOException {

        EportalScrapperService.loginToEportal(login, password);
        OkHttpClient client = EportalScrapperService.getClient();

        Document page = getDocumentFromUrl(client, "https://eportal.pwr.edu.pl/calendar/view.php?view=month");

        String buttonClassName;

        if(offset < 0){
            buttonClassName = "arrow_link previous";
        } else {
            buttonClassName = "arrow_link next";
        }

        for(int i = 0; i < Math.abs(offset); i++){

            String nextCalendarUrl = page.getElementsByClass(buttonClassName).first().attr("href");

            page = getDocumentFromUrl(client, nextCalendarUrl);

        }

        String monthName = page.getElementsByClass("current").text();

        List<Element> daysElement = page.getElementsByClass("day");
        daysElement.removeIf(day -> day.getElementsByClass("eventname").text().equals(""));

        List<CalendarEvent> events = new ArrayList<>();

        for(Element day : daysElement) {

            String[] dateArray = day.getElementsByClass("sr-only").text().split(",");

            CalendarEvent event = CalendarEvent.builder()
                    .title(day.getElementsByClass("eventname").text())
                    .date(dateArray[dateArray.length-2] + "," + dateArray[dateArray.length-1])
                    .build();

            events.add(event);
        }


        return CalendarMonth.builder()
                .monthName(monthName)
                .events(events)
                .build();

    }

    public String getIcsCalendarUrl(String login, String password) throws IOException {

        EportalScrapperService.loginToEportal(login, password);
        OkHttpClient client = EportalScrapperService.getClient();

        Document page = getDocumentFromUrl(client, "https://eportal.pwr.edu.pl/calendar/export.php");

        String sessionKey = page.getElementsByAttributeValue("name", "sesskey").attr("value");

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "_qf__core_calendar_export_form=1&events[exportevents]=all&generateurl=Pobierz adres URL kalendarza&period[timeperiod]=recentupcoming&sesskey=" + sessionKey);
        Request request = new Request.Builder()
                .url("https://eportal.pwr.edu.pl/calendar/export.php")
                .method("POST", body)

                .build();

        Response response = client.newCall(request).execute();


        String calendarURL = Jsoup.parse(response.body().string())
                .getElementsByClass("calendarurl")
                .text()
                .replace("Adres URL kalendarza: ", "");


        return calendarURL;
    }



    @NotNull
    private Document getDocumentFromUrl(OkHttpClient client, String nextCalendarUrl) throws IOException {
        Request request;
        Document page;
        request = new Request.Builder()
                .url(nextCalendarUrl)
                .build();

        page = Jsoup.parse(client.newCall(request).execute().body().string());
        return page;
    }


}
