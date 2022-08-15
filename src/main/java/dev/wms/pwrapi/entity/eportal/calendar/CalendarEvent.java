package dev.wms.pwrapi.entity.eportal.calendar;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarEvent {

    private String title;
    private String date;

}
