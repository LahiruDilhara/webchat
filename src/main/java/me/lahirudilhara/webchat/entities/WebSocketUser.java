package me.lahirudilhara.webchat.entities;

import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;

public class WebSocketUser {
    String username;
    Instant connectedAt;
    WebSocketSession session;

    public WebSocketUser(){}

    public WebSocketUser(String username, Instant connectedAt, WebSocketSession session) {
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
