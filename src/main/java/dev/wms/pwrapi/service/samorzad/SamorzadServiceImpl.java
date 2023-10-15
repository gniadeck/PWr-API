package dev.wms.pwrapi.service.samorzad;

import dev.wms.pwrapi.dao.google.calendar.GoogleCalendarDAO;
import dev.wms.pwrapi.dto.events.EventDto;
import dev.wms.pwrapi.dto.google.converters.EventDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SamorzadServiceImpl implements SamorzadService {

    @Value("${samorzad.calendar-id}")
    private String CALENDAR_ID;

    @Value("${samorzad.calendar-key}")
    private String CALENDAR_KEY;

    private final GoogleCalendarDAO googleCalendar;
    private final EventDTOConverter eventConverter;

    @Override
    public Set<EventDto> getSamorzadEvents(LocalDateTime from, LocalDateTime to){
        return googleCalendar.getEvents(CALENDAR_ID, CALENDAR_KEY, from, to)
                .stream()
                .map(eventConverter::mapToEventDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<EventDto> getSamorzadEventsOfMonth(Month month, Year year){
        LocalDateTime from = LocalDate
                .of(year.getValue(), month.getValue(), 1)
                .atStartOfDay();

        LocalDateTime to = LocalDate
                .of(year.getValue(), month.getValue(), 1)
                .plusMonths(1)
                .atStartOfDay();

        return getSamorzadEvents(from, to);
    }

    @Override
    public Set<EventDto> getSamorzadEventsOfCurrentYearMonth(Month month){
        return getSamorzadEventsOfMonth(month, Year.now());
    }
}