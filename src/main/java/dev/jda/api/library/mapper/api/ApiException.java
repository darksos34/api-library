package dev.jda.api.library.mapper.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
public class ApiException extends RuntimeException implements Serializable {
    private final HttpStatus httpStatus;
    private final String detail;

    public ApiException(String message, HttpStatus httpStatus, String detail) {
        super(message);
        this.httpStatus = httpStatus;
        this.detail = detail;
    }

    public ApiException(String message, String detail) {
        super(message);
        this.detail = detail;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ApiException(String message, String detail, HttpStatus httpStatus) {
        super(message);
        this.detail = detail;
        this.httpStatus = httpStatus;
    }



}
