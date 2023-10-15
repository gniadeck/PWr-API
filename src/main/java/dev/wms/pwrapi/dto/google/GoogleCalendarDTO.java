package dev.wms.pwrapi.dto.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GoogleCalendarDTO extends GoogleBaseDTO {

    private String timeZone;

    @JsonProperty("items")
    private Set<GoogleEventDTO> events;
}
