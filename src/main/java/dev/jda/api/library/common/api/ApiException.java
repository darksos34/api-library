package dev.jda.api.library.common.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
public class ApiException extends RuntimeException implements Serializable {
    private final HttpStatus httpStatus;
    private String detail;

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
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
