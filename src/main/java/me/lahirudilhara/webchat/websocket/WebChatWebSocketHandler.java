package me.lahirudilhara.webchat.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ValidationException;
import me.lahirudilhara.webchat.core.util.JsonUtil;
import me.lahirudilhara.webchat.core.util.SchemaValidator;
import me.lahirudilhara.webchat.core.util.WebSocketError;
import me.lahirudilhara.webchat.dto.wc.WebSocketMessageDTO;
import me.lahirudilhara.webchat.websocket.events.ClientConnectedEvent;
import me.lahirudilhara.webchat.websocket.events.ClientDisconnectedEvent;
import me.lahirudilhara.webchat.websocket.events.OnClientMessageEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

@Component
public class WebChatWebSocketHandler extends TextWebSocketHandler {
    private final ApplicationEventPublisher applicationEventPublisher;

    public WebChatWebSocketHandler( ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        if(!session.isOpen()) return;
        if(Objects.requireNonNull(session.getPrincipal()).getName() == null) return;
        applicationEventPublisher.publishEvent(new ClientConnectedEvent(session.getPrincipal().getName(),session));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        if(!session.isOpen()) return;
        if(Objects.requireNonNull(session.getPrincipal()).getName() == null) return;
        try{
            WebSocketMessageDTO webSocketMessageDTO = JsonUtil.jsonToObject(message.getPayload(), WebSocketMessageDTO.class);
            SchemaValidator.validate(webSocketMessageDTO);
            applicationEventPublisher.publishEvent(new OnClientMessageEvent(session.getPrincipal().getName(),webSocketMessageDTO));
        }
        catch(JsonProcessingException e){
            WebSocketError.sendError(session,"The message json is not in correct format");
        }
        catch(ValidationException e){
            WebSocketError.sendError(session,e.getMessage());
        }
        catch (Exception e) {
            WebSocketError.sendError(session,"Unknown error occurred");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        if(Objects.requireNonNull(session.getPrincipal()).getName() == null) return;
        applicationEventPublisher.publishEvent(new ClientDisconnectedEvent(session.getPrincipal().getName()));
    }
}
