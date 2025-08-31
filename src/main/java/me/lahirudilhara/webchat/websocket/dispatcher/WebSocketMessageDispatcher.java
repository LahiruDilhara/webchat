package me.lahirudilhara.webchat.websocket.dispatcher;

import me.lahirudilhara.webchat.dto.wc.WebSocketMessageDTO;
import me.lahirudilhara.webchat.websocket.handlers.MessageHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebSocketMessageDispatcher {
    private final Map<Class<? extends WebSocketMessageDTO>, MessageHandler<? extends WebSocketMessageDTO>> userMessageHandlerMap = new HashMap<>();

    public WebSocketMessageDispatcher(List<MessageHandler<? extends WebSocketMessageDTO>> messageHandlers) {
        for (MessageHandler<? extends WebSocketMessageDTO> messageHandler : messageHandlers) {
            userMessageHandlerMap.put(messageHandler.getMessageType(), messageHandler);
        }
    }

    public <T extends WebSocketMessageDTO> void dispatch(T message, String senderUsername) {
        MessageHandler<T> messageHandler = (MessageHandler<T>) userMessageHandlerMap.get(message.getClass());
        if (messageHandler == null) {
            throw new IllegalArgumentException("No handler found for"+ messageHandler.getClass().getName());
        }
        messageHandler.handleMessage(message,senderUsername);
    }
}
