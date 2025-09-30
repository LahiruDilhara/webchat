package me.lahirudilhara.webchat.websocket.dispatcher;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.dto.requests.BaseRequestMessageDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MessageDispatcher{
    private final Map<Class<? extends BaseRequestMessageDTO>, MessageHandler<? extends BaseRequestMessageDTO>> userMessageHandlerMap = new ConcurrentHashMap<>();

    public MessageDispatcher(List<MessageHandler<? extends BaseRequestMessageDTO>> messageHandlers) {
        for (MessageHandler<? extends BaseRequestMessageDTO> messageHandler : messageHandlers) {
            userMessageHandlerMap.put(messageHandler.getMessageClassType(), messageHandler);
        }
    }

    public <T extends BaseRequestMessageDTO> void dispatch(T message, String senderUsername, String sessionId) {
        MessageHandler<T> messageHandler = (MessageHandler<T>) userMessageHandlerMap.get(message.getClass());
        if(messageHandler == null) {
            log.error("There is no handler exists for message type {}", message.getClass());
            return;
        }
        messageHandler.handleMessage(message,senderUsername,sessionId);
    }
}
