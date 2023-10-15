package dev.wms.pwrapi.dao.eportal;


import dev.wms.pwrapi.dao.auth.AuthDao;
import dev.wms.pwrapi.entity.eportal.calendar.CalendarEvent;
import dev.wms.pwrapi.entity.eportal.calendar.CalendarMonth;
import dev.wms.pwrapi.utils.http.HttpClient;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class EportalCalendarDAO {

    private final AuthDao eportalAuthDao;

    public CalendarMonth getEventsWithOffset(String login, String password, int offset) throws IOException {

        HttpClient client = eportalAuthDao.login(login, password);

        Document page = client.getDocument("https://eportal.pwr.edu.pl/calendar/view.php?view=month");

        String buttonClassName = getButtonClassName(offset);

        for(int i = 0; i < Math.abs(offset); i++){
            String nextCalendarUrl = page.getElementsByClass(buttonClassName).first().attr("href");
            page = client.getDocument(nextCalendarUrl);
        }

        String monthName = page.getElementsByClass("current").text();

        List<Element> days = page.getElementsByClass("day");
        days.removeIf(day -> day.getElementsByClass("eventname").text().equals(""));

        List<CalendarEvent> events = new ArrayList<>();

        for(Element day : days) {

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

    @NotNull
    private String getButtonClassName(int offset) {
        return offset < 0 ? "arrow_link previous" : "arrow_link next";
    }

    public String getIcsCalendarUrl(String login, String password) throws IOException {

        HttpClient client = eportalAuthDao.login(login, password);

        Document page = client.getDocument("https://eportal.pwr.edu.pl/calendar/export.php");

        String sessionKey = page.getElementsByAttributeValue("name", "sesskey").attr("value");

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "_qf__core_calendar_export_form=1&events[exportevents]=all&generateurl=Pobierz adres URL kalendarza&period[timeperiod]=recentupcoming&sesskey=" + sessionKey);
        Request request = new Request.Builder()
                .url("https://eportal.pwr.edu.pl/calendar/export.php")
                .method("POST", body)
                .build();

        return client.getDocument(request)
                .getElementsByClass("calendarurl")
                .text()
                .replace("Adres URL kalendarza: ", "");
    }

}
