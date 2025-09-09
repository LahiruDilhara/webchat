package me.lahirudilhara.webchat.websocket.events;

import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;

public record NewClientJoinedEvent(String username, WebSocketSession session, int roomId, Instant joinedAt) {
}
