package dev.wms.pwrapi.dto.google;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.wms.pwrapi.utils.common.DateFormats;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class GoogleBaseDTO {

    private String kind;
    private String etag;
    private String summary;

    @JsonFormat(pattern = DateFormats.RFC3339_WITH_MILLISECONDS)
    private ZonedDateTime created;
}
