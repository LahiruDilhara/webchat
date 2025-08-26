package me.lahirudilhara.webchat.service.websocket;

import me.lahirudilhara.webchat.dto.websocket.user.UserBaseMessageDto;
import me.lahirudilhara.webchat.websocket.UserMessageHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebSocketMessageDispatcher {
    private final Map<Class<? extends UserBaseMessageDto>, UserMessageHandler<? extends UserBaseMessageDto>> userMessageHandlerMap = new HashMap<>();

    public WebSocketMessageDispatcher(List<UserMessageHandler<? extends UserBaseMessageDto>> userMessageHandlers) {
        for (UserMessageHandler<? extends UserBaseMessageDto> userMessageHandler : userMessageHandlers) {
            userMessageHandlerMap.put(userMessageHandler.getMessageType(), userMessageHandler);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends UserBaseMessageDto> void dispatch(T message) {
        UserMessageHandler<T> userMessageHandler = (UserMessageHandler<T>) userMessageHandlerMap.get(message.getClass());
        if (userMessageHandler == null) {
            throw new IllegalArgumentException("No handler found for"+userMessageHandler.getClass().getName());
        }
        userMessageHandler.handleMessage(message);
    }
}
