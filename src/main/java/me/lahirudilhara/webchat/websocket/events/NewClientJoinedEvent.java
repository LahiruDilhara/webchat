package me.lahirudilhara.webchat.websocket.events;

import org.springframework.web.socket.WebSocketSession;

public record NewClientJoinedEvent(String username, WebSocketSession session, int roomId) {
}
