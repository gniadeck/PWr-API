package dev.wms.pwrapi.utils.parking;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ParkingDateUtils {

    public static LocalDateTime getDateTimeInPoland(){
        return ZonedDateTime.now()
                .withZoneSameInstant(ZoneId.of("Europe/Warsaw"))
                .toLocalDateTime();
    }

}
