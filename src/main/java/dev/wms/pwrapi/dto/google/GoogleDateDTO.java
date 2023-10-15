package dev.wms.pwrapi.dto.google;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.wms.pwrapi.utils.common.DateFormats;
import lombok.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Google returns either:
 * <p><b>- date</b> for all-day events
 * <p><b>- dateTime</b> and <b>timeZone</b> for events with start and end date<p/>
 *
 * <p>Dates are provided in <b>UTC</b> time zone<p/>
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GoogleDateDTO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = DateFormats.RFC3339)
    private ZonedDateTime dateTime;

    private String timeZone;

    public Optional<LocalDate> getDate() {
        return Optional.ofNullable(date);
    }

    public Optional<ZonedDateTime> getDateTime() {
        return Optional.ofNullable(dateTime);
    }

    public Optional<String> getTimeZone() {
        return Optional.ofNullable(timeZone);
    }
}
