package me.lahirudilhara.webchat.websocket.events;

import me.lahirudilhara.webchat.dto.wc.WebSocketMessageDTO;
import org.springframework.web.socket.WebSocketSession;

public record ClientMessageEvent(String username, WebSocketMessageDTO messageDTO, WebSocketSession session) {
}
