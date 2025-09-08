package me.lahirudilhara.webchat.websocket.events;

import me.lahirudilhara.webchat.dto.wc.WebSocketError;
import org.springframework.web.socket.WebSocketSession;

public record ClientErrorEvent(WebSocketError error, String username) {

}
