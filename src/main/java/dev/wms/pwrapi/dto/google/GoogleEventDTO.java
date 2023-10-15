package dev.wms.pwrapi.dto.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GoogleEventDTO extends GoogleBaseDTO {

    private String id;
    private String status;
    private String htmlLink;
    private String location;
    private String description;

    @JsonProperty("start")
    private GoogleDateDTO startDate;

    @JsonProperty("end")
    private GoogleDateDTO endDate;

    public Optional<String> getLocation(){
        return Optional.ofNullable(location);
    }

    public Optional<String> getDescription(){
        return Optional.ofNullable(description);
    }
}