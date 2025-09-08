package me.lahirudilhara.webchat.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.common.util.JsonUtil;
import me.lahirudilhara.webchat.common.util.SchemaValidator;
import me.lahirudilhara.webchat.common.util.WebSocketError;
import me.lahirudilhara.webchat.dto.wc.WebSocketMessageDTO;
import me.lahirudilhara.webchat.websocket.events.ClientConnectedEvent;
import me.lahirudilhara.webchat.websocket.events.ClientDisconnectedEvent;
import me.lahirudilhara.webchat.websocket.events.ClientMessageEvent;
import me.lahirudilhara.webchat.websocket.session.SessionManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

@Slf4j
@Component
public class WebChatWebSocketHandler extends TextWebSocketHandler {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SessionManager sessionManager;

    public WebChatWebSocketHandler( ApplicationEventPublisher applicationEventPublisher, SessionManager sessionManager) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.sessionManager =sessionManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        if(!session.isOpen()) return;
        if(Objects.requireNonNull(session.getPrincipal()).getName() == null) return;
        try{
            sessionManager.onUserConnected(session.getPrincipal().getName(),session);
            applicationEventPublisher.publishEvent(new ClientConnectedEvent(session.getPrincipal().getName(),session));
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        if(!session.isOpen()) return;
        if(Objects.requireNonNull(session.getPrincipal()).getName() == null) return;
        try{
            WebSocketMessageDTO webSocketMessageDTO = JsonUtil.jsonToObject(message.getPayload(), WebSocketMessageDTO.class);
            SchemaValidator.validate(webSocketMessageDTO);
            applicationEventPublisher.publishEvent(new ClientMessageEvent(session.getPrincipal().getName(),webSocketMessageDTO));
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
        try{
            sessionManager.onUserDisconnected(session.getPrincipal().getName());
            applicationEventPublisher.publishEvent(new ClientDisconnectedEvent(session.getPrincipal().getName()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // using this connection close, can save the user last seen time
    }
}
