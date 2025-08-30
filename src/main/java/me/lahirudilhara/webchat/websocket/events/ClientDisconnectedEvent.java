package me.lahirudilhara.webchat.websocket.events;

public class ClientDisconnectedEvent {
    private final String username;

    public ClientDisconnectedEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
