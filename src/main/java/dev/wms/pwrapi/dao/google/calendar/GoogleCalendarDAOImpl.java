package dev.wms.pwrapi.dao.google.calendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.wms.pwrapi.dto.google.GoogleCalendarDTO;
import dev.wms.pwrapi.dto.google.GoogleEventDTO;
import dev.wms.pwrapi.utils.common.DateUtils;
import dev.wms.pwrapi.utils.http.HttpClient;
import dev.wms.pwrapi.utils.http.helpers.ResponseAndStatus;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TimeZone;

@Repository
@RequiredArgsConstructor
public class GoogleCalendarDAOImpl implements GoogleCalendarDAO {

    @Value("${google.calendar.base-url}")
    private String CALENDAR_BASE_URL;

    private final ObjectMapper objectMapper;

    @Override
    public Set<GoogleEventDTO> getEvents(String calendarId, String apiKey, LocalDateTime from, LocalDateTime to){
        String url = createUrl(calendarId, apiKey, from, to);

        ResponseAndStatus response = new HttpClient().getStringAndStatusCode(url);

        try{
            checkResponse(response.getStatusCode(), url);
            return objectMapper.readValue(response.getResponseBody(), GoogleCalendarDTO.class).getEvents();
        } catch (JsonProcessingException e){
            throw new RuntimeException("Error while parsing response from Google Calendar API. Request url: " + url, e);
        }
    }

    private String createUrl(String calendarId, String apiKey){
        return CALENDAR_BASE_URL + "/" + calendarId + "/events?"
                + "calendarId=" + calendarId + "%40group.calendar.google.com"
                + "&showDeleted=false"      // request will not return events with "status": "cancelled"
                + "&singleEvents=true"      // request will return recurring events as single events
                + "&key=" + apiKey
                + "&timeZone=Europe%2FWarsaw";
    }

    private String createUrl(String calendarId, String apiKey, LocalDateTime from, LocalDateTime to){
        TimeZone UTC = TimeZone.getTimeZone("UTC");
        TimeZone Warsaw = TimeZone.getTimeZone("Europe/Warsaw");

        return createUrl(calendarId, apiKey)
                + "&timeMin=" + DateUtils.formatToRFC3339(from, Warsaw, UTC)
                + "&timeMax=" + DateUtils.formatToRFC3339(to, Warsaw, UTC);
    }

    private void checkResponse(int status, String url){
        if(status != 200) {
            throw new RuntimeException(
                    String.format(
                            "Bad response. Response code: %d. Url: %s",
                            status,
                            url
                    )
            );
        }
    }
}
