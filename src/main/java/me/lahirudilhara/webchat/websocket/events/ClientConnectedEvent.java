package me.lahirudilhara.webchat.websocket.events;

import org.springframework.web.socket.WebSocketSession;

public record ClientConnectedEvent(String username, WebSocketSession session) {
}
