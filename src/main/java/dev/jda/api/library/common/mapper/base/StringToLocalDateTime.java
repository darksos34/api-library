package dev.jda.api.library.common.mapper.base;

import dev.jda.api.library.common.exception.DateMapperException;

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

    @Override
    public LocalDateTime apply(String s) {
        if (s == null) {
            return null;
        }

        try {
            TemporalAccessor temporalAccessor = this.formatter.parseBest(s, LocalDateTime::from, LocalDate::from);

            if (temporalAccessor instanceof LocalDateTime ldt) {
                return ldt;
            }

            if (temporalAccessor instanceof LocalDate ld) {
                return LocalDateTime.of(ld, LocalTime.NOON);
            }

            throw new DateMapperException("Parsed temporal accessor is not supported: " + temporalAccessor.getClass());
        } catch (DateTimeParseException e) {
            throw new DateMapperException(String.format("Error parsing date '%s': %s", s, e.getMessage()), e);
        }
    }
}
