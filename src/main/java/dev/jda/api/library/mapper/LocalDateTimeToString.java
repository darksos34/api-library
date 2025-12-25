package dev.jda.api.library.mapper;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

public class LocalDateTimeToString implements Function<LocalDateTime, String> {

    private final DateTimeFormatter formatter;
    private final String timeOffset;

    public LocalDateTimeToString(DateTimeFormatter formatter, String timeOffset) {
        this.formatter = formatter;
        this.timeOffset = timeOffset;
    }

    public LocalDateTimeToString(DateTimeFormatter formatter) {
        this.formatter = formatter;
        this.timeOffset = null;
    }


    public String apply(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        } else {
            try {
                return this.parseDateTime(localDateTime);
            } catch (DateTimeException e) {
                try {
                    throw new DateMapperException(String.format("Error parsing date %s", e.getMessage()));
                } catch (DateMapperException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private String parseDateTime(LocalDateTime localDateTime) {
        return this.timeOffset != null ? localDateTime.atZone(ZoneId.of(this.timeOffset)).truncatedTo(ChronoUnit.SECONDS).format(this.formatter) : localDateTime.truncatedTo(ChronoUnit.SECONDS).format(this.formatter);
    }
}
