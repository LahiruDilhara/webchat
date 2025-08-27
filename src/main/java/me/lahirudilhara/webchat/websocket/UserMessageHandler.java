package me.lahirudilhara.webchat.websocket;

import me.lahirudilhara.webchat.dto.websocket.user.UserBaseMessageDto;

public interface UserMessageHandler<T extends UserBaseMessageDto> {
    Class<T> getMessageType();

    void handleMessage(T message,String senderUsername);
}
