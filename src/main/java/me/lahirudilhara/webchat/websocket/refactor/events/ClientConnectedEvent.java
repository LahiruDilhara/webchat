package me.lahirudilhara.webchat.websocket.refactor.events;

import org.springframework.web.socket.WebSocketSession;

public record ClientConnectedEvent(String username, WebSocketSession session) {
}
