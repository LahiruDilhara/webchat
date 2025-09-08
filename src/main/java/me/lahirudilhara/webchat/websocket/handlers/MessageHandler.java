package me.lahirudilhara.webchat.websocket.handlers;

import me.lahirudilhara.webchat.dto.wc.WebSocketMessageDTO;
import org.springframework.web.socket.WebSocketSession;

public interface MessageHandler<T extends WebSocketMessageDTO> {
    Class<T> getMessageType();

    void handleMessage(T message, String senderUsername, WebSocketSession session);
}
