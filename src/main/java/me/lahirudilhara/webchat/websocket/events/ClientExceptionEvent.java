package me.lahirudilhara.webchat.websocket.events;

public class ClientExceptionEvent {
    private final String username;
    private final String message;

    public ClientExceptionEvent(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
