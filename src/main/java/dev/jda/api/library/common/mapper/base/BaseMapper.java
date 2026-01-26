package dev.jda.api.library.common.mapper.base;

import dev.jda.api.library.common.api.ApiException;
import org.modelmapper.Converter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public abstract class BaseMapper {
    protected final LocalDateTimeToString dateTimeToStringConverter = new LocalDateTimeToString(DateTimeFormatter.ISO_OFFSET_DATE_TIME, ZoneId.systemDefault().getId());
    protected final StringToLocalDateTime offsetDateTimeStringToDateConverter = new StringToLocalDateTime(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    protected final Converter<String, LocalDateTime> stringToLocalDateTime = ctx -> {
        try {
            return offsetDateTimeStringToDateConverter.apply(ctx.getSource().trim());

        } catch (Exception e) {
            throw new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    };

    protected final Converter<LocalDateTime, String> localDateTimeToString = ctx -> dateTimeToStringConverter.apply(ctx.getSource());

}
