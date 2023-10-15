package dev.wms.pwrapi.dao.google.calendar;

import dev.wms.pwrapi.dto.google.GoogleEventDTO;

import java.time.LocalDateTime;
import java.util.Set;

public interface GoogleCalendarDAO {

    /**
     * Method that returns some events from Google calendar.
     * @param calendarId - id of calendar from which events should be returned
     * @param apiKey - key (token) required by Google API to get authenticated
     * @param from - date for Europe/Warsaw time zone
     * @param to - date for Europe/Warsaw time zone
     *
     * @return events where date is specified in the UTC time zone
     * */
    Set<GoogleEventDTO> getEvents(String calendarId, String apiKey, LocalDateTime from, LocalDateTime to);
}
