package me.lahirudilhara.webchat.core.lib;

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
