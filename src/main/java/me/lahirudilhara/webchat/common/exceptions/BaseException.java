package me.lahirudilhara.webchat.common.exceptions;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;

    public BaseException(String message,HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
