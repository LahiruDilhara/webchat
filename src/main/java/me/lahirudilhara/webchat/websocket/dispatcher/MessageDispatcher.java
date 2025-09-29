package me.lahirudilhara.webchat.websocket.dispatcher;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.dto.WebSocketMessageDTO;
import me.lahirudilhara.webchat.websocket.lib.interfaces.IncomingMessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MessageDispatcher{
    private final Map<Class<? extends WebSocketMessageDTO>, MessageHandler<? extends WebSocketMessageDTO>> userMessageHandlerMap = new ConcurrentHashMap<>();

    public MessageDispatcher(List<MessageHandler<? extends WebSocketMessageDTO>> messageHandlers) {
        for (MessageHandler<? extends WebSocketMessageDTO> messageHandler : messageHandlers) {
            userMessageHandlerMap.put(messageHandler.getMessageClassType(), messageHandler);
        }
    }

    public <T extends WebSocketMessageDTO> void dispatch(T message, String senderUsername, String sessionId) {
        MessageHandler<T> messageHandler = (MessageHandler<T>) userMessageHandlerMap.get(message.getClass());
        if(messageHandler == null) {
            log.error("There is no handler exists for message type {}", message.getClass());
            return;
        }
        messageHandler.handleMessage(message,senderUsername,sessionId);
    }
}
