package me.lahirudilhara.webchat.websocket.refactor.events;

import me.lahirudilhara.webchat.websocket.dto.WebSocketMessageDTO;

public record ClientMessageEvent(String username, String sessionId, WebSocketMessageDTO messageDTO) {
}
