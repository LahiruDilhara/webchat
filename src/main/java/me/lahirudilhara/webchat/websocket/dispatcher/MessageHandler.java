package me.lahirudilhara.webchat.websocket.dispatcher;

import me.lahirudilhara.webchat.websocket.dto.WebSocketMessageDTO;

public interface MessageHandler<T extends WebSocketMessageDTO> {
    Class<T> getMessageClassType();

    void handleMessage(T message,String senderUsername, String sessionId);
}
