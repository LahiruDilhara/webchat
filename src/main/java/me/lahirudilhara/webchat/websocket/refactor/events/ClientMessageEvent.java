package me.lahirudilhara.webchat.websocket.refactor.events;

import me.lahirudilhara.webchat.websocket.dto.requests.BaseRequestMessageDTO;

public record ClientMessageEvent(String username, String sessionId, BaseRequestMessageDTO messageDTO) {
}
