package me.lahirudilhara.webchat.websocket.events;

import org.springframework.web.socket.WebSocketSession;

public class ClientConnectedEvent {
    private final String username;
    private final WebSocketSession session;

    public ClientConnectedEvent(String username, WebSocketSession session) {
        this.username = username;
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public WebSocketSession getSession() {
        return session;
    }
}
