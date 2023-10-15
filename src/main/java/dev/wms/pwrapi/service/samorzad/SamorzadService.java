package dev.wms.pwrapi.service.samorzad;

import dev.wms.pwrapi.dto.events.EventDto;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Set;


/**
 * Get Samorzad PWR events from their Google Calendar:
 * <p><a href="https://samorzad.pwr.edu.pl/wydarzenia">See website</a></p>
 */
public interface SamorzadService {

    /**
     * @param from - date from which events should be returned
     * @param to - date to which events should be returned
     * */
    Set<EventDto> getSamorzadEvents(LocalDateTime from, LocalDateTime to);

    /**
     * @return returns events from the first day of the month to the last day of the month
     * of current year.
     * */
    Set<EventDto> getSamorzadEventsOfCurrentYearMonth(Month month);

    /**
     * @return events from the first day of the month to the last day of the month
     * of given year
     * */
    Set<EventDto> getSamorzadEventsOfMonth(Month month, Year year);
}
