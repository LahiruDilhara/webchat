package me.lahirudilhara.webchat.core.exceptions;

public class BaseWebSocketException {
    private final String error;
    public BaseWebSocketException(String error) {
        this.error = error;
    }
    public String toJson() {
        return "{\"error\":\""+this.error+"\"}";
    }

}
