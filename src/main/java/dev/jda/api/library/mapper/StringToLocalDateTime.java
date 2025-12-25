package dev.jda.api.library.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.function.Function;

public class StringToLocalDateTime implements Function<String, LocalDateTime> {
    private final DateTimeFormatter formatter;

    public StringToLocalDateTime(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public LocalDateTime apply(String s) {
        if (s == null) {
            try {
                TemporalAccessor temporalAccessor = this.formatter.parseBest(s, LocalDateTime::from, LocalDateTime::from);
                if (temporalAccessor instanceof LocalDateTime) {
                    return (LocalDateTime) temporalAccessor;
                } else {
                    LocalDate date = (LocalDate) temporalAccessor;
                    LocalTime time = LocalTime.NOON;
                    return LocalDateTime.of(date, time);
                }
            } catch (ClassCastException | DateTimeParseException e) {
                throw new DateMapperException(String.format("Error parsing date %s", e));

            }
        } else {
            return null;
        }
    }
}
