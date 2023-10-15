package dev.wms.pwrapi.utils.common;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.TimeZone;

public final class DateUtils {

    private static final DateTimeFormatter ISO_FORMATTER;
    private static final DateTimeFormatter SIMPLE_DATE_FORMATTER;
    private static final DateTimeFormatter TIME_FORMATTER;

    static{
        ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        SIMPLE_DATE_FORMATTER = DateTimeFormatter.ofPattern(DateFormats.SIMPLE);
        TIME_FORMATTER = DateTimeFormatter.ofPattern(DateFormats.TIME);
    }

    private DateUtils() {}

    public static ZonedDateTime convertTimeZones(ZonedDateTime date, TimeZone timeZoneToConvertTo) {
        return date.withZoneSameInstant(timeZoneToConvertTo.toZoneId());
    }

    public static ZonedDateTime convertTimeZones(
            LocalDateTime date,
            TimeZone currentTimeZone,
            TimeZone timeZoneToConvertTo) {

        ZonedDateTime zonedDateTime = date.atZone(currentTimeZone.toZoneId());
        return convertTimeZones(zonedDateTime, timeZoneToConvertTo);
    }

    public static String formatToRFC3339(ZonedDateTime date){
        return date.format(ISO_FORMATTER);
    }

    public static String formatToRFC3339(LocalDateTime date, TimeZone current, TimeZone timeZoneToConvertTo){
        return formatToRFC3339(convertTimeZones(date, current, timeZoneToConvertTo));
    }

    public static String formatToDate(ZonedDateTime date){
        return date.format(SIMPLE_DATE_FORMATTER);
    }

    public static String formatToDate(LocalDateTime date){
        return date.format(SIMPLE_DATE_FORMATTER);
    }

    public static String formatToDate(LocalDate date){
        return date.format(SIMPLE_DATE_FORMATTER);
    }

    public static String formatToTime(ZonedDateTime date){
        return date.format(TIME_FORMATTER);
    }

    public static String formatToTime(LocalDateTime date){
        return date.format(TIME_FORMATTER);
    }

    public static String formatToLocalizedDate(LocalDate date, FormatStyle formatStyle, Locale locale){
        return DateTimeFormatter.ofLocalizedDate(formatStyle).withLocale(locale).format(date);
    }
}
