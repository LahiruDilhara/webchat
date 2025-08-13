package me.lahirudilhara.webchat.core.lib;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private String message;
    private HttpStatus code;

    public ErrorResponse(String message, HttpStatus code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }
}
