package dev.wms.pwrapi.dto.events;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    private String title;
    private String date;
    private String time;
    private String place;
    private String description;

}
