package me.lahirudilhara.webchat.common.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private String error;
    private HttpStatus code;
    private static final ObjectMapper mapper = new ObjectMapper();

    public ErrorResponse(String message, HttpStatus code) {
        this.error = message;
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }
}
