package dev.wms.pwrapi.dto.isjsosdown;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.wms.pwrapi.utils.config.LocalDateTimeFromMillisDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDateTime;

@Value(staticConstructor = "of")
@Getter
@Builder
@AllArgsConstructor
public class DowntimeDTO {
    @JsonDeserialize(using = LocalDateTimeFromMillisDeserializer.class)
    LocalDateTime downSince;
    @JsonDeserialize(using = LocalDateTimeFromMillisDeserializer.class)
    LocalDateTime downTill;

    public DowntimeDTO() {
        downSince = LocalDateTime.now();
        downTill = LocalDateTime.now();
    }

}
