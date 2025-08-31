package me.lahirudilhara.webchat.common.exceptions;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final HttpStatus code;

    public BaseException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }

    public HttpStatus getCode() {
        return code;
    }
}
