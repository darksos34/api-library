package dev.jda.api.library.mapper;

import lombok.Getter;

@Getter
public class DateMapperException extends RuntimeException {
    private String message;
    public DateMapperException(String message) {
        this.message = message;

    }
}
