package me.lahirudilhara.webchat.websocket.events;

import me.lahirudilhara.webchat.dto.wc.WebSocketMessageDTO;

public record ClientMessageEvent(String username, WebSocketMessageDTO messageDTO) {
}
