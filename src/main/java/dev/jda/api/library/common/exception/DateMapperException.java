package dev.jda.api.library.common.exception;

public class DateMapperException extends RuntimeException {
    public DateMapperException(String message) {
        super(message);
    }

    public DateMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateMapperException(Throwable cause) {
        super(cause);
    }
}
