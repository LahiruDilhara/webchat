package me.lahirudilhara.webchat.common.types;

public class WebSocketErrorResponse {
    private String error;

    public WebSocketErrorResponse() {
    }

    public WebSocketErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
