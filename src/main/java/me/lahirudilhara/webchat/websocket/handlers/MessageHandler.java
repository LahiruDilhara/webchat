package me.lahirudilhara.webchat.websocket.handlers;

import me.lahirudilhara.webchat.dto.wc.WebSocketMessageDTO;

public interface MessageHandler<T extends WebSocketMessageDTO> {
    Class<T> getMessageType();

    void handleMessage(T message,String senderUsername);
}
