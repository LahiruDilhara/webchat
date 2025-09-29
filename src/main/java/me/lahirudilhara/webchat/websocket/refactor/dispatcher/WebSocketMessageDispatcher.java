package me.lahirudilhara.webchat.websocket.refactor.dispatcher;

import me.lahirudilhara.webchat.websocket.dto.WebSocketError;
import me.lahirudilhara.webchat.websocket.dto.WebSocketMessageDTO;
import me.lahirudilhara.webchat.websocket.refactor.events.ClientErrorEvent;
import me.lahirudilhara.webchat.websocket.refactor.events.ClientMessageEvent;
import me.lahirudilhara.webchat.websocket.refactor.handlers.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebSocketMessageDispatcher {
    private final ApplicationEventPublisher applicationEventPublisher;
    private static final Logger log = LoggerFactory.getLogger(WebSocketMessageDispatcher.class);
    private final Map<Class<? extends WebSocketMessageDTO>, MessageHandler<? extends WebSocketMessageDTO>> userMessageHandlerMap = new HashMap<>();

    public WebSocketMessageDispatcher(List<MessageHandler<? extends WebSocketMessageDTO>> messageHandlers, ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        for (MessageHandler<? extends WebSocketMessageDTO> messageHandler : messageHandlers) {
            userMessageHandlerMap.put(messageHandler.getMessageType(), messageHandler);
        }
    }

    public <T extends WebSocketMessageDTO> void dispatch(T message, String senderUsername, String sessionId) {
        MessageHandler<T> messageHandler = (MessageHandler<T>) userMessageHandlerMap.get(message.getClass());
        if (messageHandler == null) {
            log.error("The message handler not found. The username is {}. The message class is {}",senderUsername,message.getClass());
            applicationEventPublisher.publishEvent(new ClientErrorEvent(new WebSocketError("Internal error occurred"),senderUsername,sessionId));
            return;
        }
        messageHandler.handleMessage(message,senderUsername, sessionId);
    }

    @Async
    @EventListener
    public void OnClientMessage(ClientMessageEvent event){
        this.dispatch(event.messageDTO(),event.username(),event.sessionId());
    }
}
