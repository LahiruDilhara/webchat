package me.lahirudilhara.webchat.websocket.session;

import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;

public class WebSocketUserSession {
    String username;
    Instant connectedAt;
    WebSocketSession session;

    public WebSocketUserSession(){}

    public WebSocketUserSession(String username, Instant connectedAt, WebSocketSession session) {
        this.username = username;
        this.connectedAt = connectedAt;
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(Instant connectedAt) {
        this.connectedAt = connectedAt;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }
}
