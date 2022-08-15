package dev.wms.pwrapi.entity.eportal.calendar;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CalendarMonth {

    private String monthName;
    private List<CalendarEvent> events;

}
