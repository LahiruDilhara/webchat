package me.lahirudilhara.webchat.websocket.dispatcher;

import me.lahirudilhara.webchat.websocket.dto.requests.BaseRequestMessageDTO;

public interface MessageHandler<T extends BaseRequestMessageDTO> {
    Class<T> getMessageClassType();

    void handleMessage(T message,String senderUsername, String sessionId);
}
