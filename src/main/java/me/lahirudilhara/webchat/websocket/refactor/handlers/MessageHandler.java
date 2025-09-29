package me.lahirudilhara.webchat.websocket.refactor.handlers;

import me.lahirudilhara.webchat.websocket.dto.WebSocketMessageDTO;

public interface MessageHandler<T extends WebSocketMessageDTO> {
    Class<T> getMessageType();

    void handleMessage(T message,String senderUsername, String sessionId);
}
