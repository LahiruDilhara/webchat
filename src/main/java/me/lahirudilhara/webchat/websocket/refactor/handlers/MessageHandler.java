package me.lahirudilhara.webchat.websocket.refactor.handlers;

import me.lahirudilhara.webchat.websocket.dto.requests.BaseRequestMessageDTO;

public interface MessageHandler<T extends BaseRequestMessageDTO> {
    Class<T> getMessageType();

    void handleMessage(T message,String senderUsername, String sessionId);
}
