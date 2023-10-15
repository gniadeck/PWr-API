package dev.wms.pwrapi.dto.google.converters;

import dev.wms.pwrapi.dto.events.EventDto;
import dev.wms.pwrapi.dto.google.GoogleEventDTO;
import dev.wms.pwrapi.utils.common.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.TimeZone;

@Service
public class EventDTOConverter {

    @Value("${default-timezone-name}")
    private String DEFAULT_TIMEZONE_NAME;

    public EventDto mapToEventDto(GoogleEventDTO googleEvent) {
        EventDto event = EventDto.builder()
                .title(googleEvent.getSummary())
                .place(googleEvent.getLocation().orElse(""))
                .description(googleEvent.getDescription().orElse(""))
                .build();

        if(eventIsWithinDay(googleEvent)){
            setAllDayEventDate(googleEvent.getStartDate().getDate().get(), event);
        }
        else if(eventHaveStartEndTime(googleEvent)) {
            setTimedEventDate(
                    googleEvent.getStartDate().getDateTime().get(),
                    extractTimeZoneOrSetDefault(googleEvent),
                    event
            );
        }
        return event;
    }

    private boolean eventIsWithinDay(GoogleEventDTO googleEvent){
        return googleEvent.getStartDate().getDate().isPresent();
    }

    private boolean eventHaveStartEndTime(GoogleEventDTO googleEvent){
        return googleEvent.getStartDate().getDateTime().isPresent();
    }

    private TimeZone extractTimeZoneOrSetDefault(GoogleEventDTO googleEvent){
        return TimeZone.getTimeZone(
                googleEvent
                        .getStartDate()
                        .getTimeZone()
                        .orElse(DEFAULT_TIMEZONE_NAME)
        );
    }

    private void setAllDayEventDate(LocalDate googleDate, EventDto event){
        event.setDate(DateUtils.formatToDate(googleDate));
        event.setTime("");
    }

    private void setTimedEventDate(ZonedDateTime googleDateTime, TimeZone timeZone, EventDto eventDto){

        // Converting time zone
        googleDateTime = googleDateTime.withZoneSameInstant(timeZone.toZoneId());

        eventDto.setDate(DateUtils.formatToDate(googleDateTime));
        eventDto.setTime(DateUtils.formatToTime(googleDateTime));
    }
}
