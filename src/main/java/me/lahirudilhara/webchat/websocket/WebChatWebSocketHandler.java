package me.lahirudilhara.webchat.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.common.util.JsonUtil;
import me.lahirudilhara.webchat.common.util.SchemaValidator;
import me.lahirudilhara.webchat.websocket.dto.response.WebSocketError;
import me.lahirudilhara.webchat.websocket.dto.requests.BaseRequestMessageDTO;
import me.lahirudilhara.webchat.websocket.refactor.events.ClientErrorEvent;
import me.lahirudilhara.webchat.websocket.refactor.events.ClientMessageEvent;
import me.lahirudilhara.webchat.websocket.refactor.session.SessionManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

@Slf4j
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
            sessionManager.onUserConnection(session.getPrincipal().getName(),session);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        if(!session.isOpen()) return;
        String username = Objects.requireNonNull(session.getPrincipal()).getName();
        if(username == null) return;
        try{
            BaseRequestMessageDTO baseRequestMessageDTO = JsonUtil.jsonToObject(message.getPayload(), BaseRequestMessageDTO.class);
            SchemaValidator.validate(baseRequestMessageDTO);
            applicationEventPublisher.publishEvent(new ClientMessageEvent(session.getPrincipal().getName(), session.getId(), baseRequestMessageDTO));
        }
        catch(JsonProcessingException e){
            System.out.println(e.getMessage());
            applicationEventPublisher.publishEvent(new ClientErrorEvent(new WebSocketError("The message json is not in correct format"),username,session.getId()));
        }
        catch(ValidationException e){
            System.out.println(e.getMessage());
            applicationEventPublisher.publishEvent(new ClientErrorEvent(new WebSocketError(e.getMessage()),username,session.getId()));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            applicationEventPublisher.publishEvent(new ClientErrorEvent(new WebSocketError("Unknown error occurred"),username,session.getId()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        if(Objects.requireNonNull(session.getPrincipal()).getName() == null) return;
        try{
            sessionManager.onUserDisconnection(session.getPrincipal().getName(),session.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // using this connection close, can save the user last seen time
    }
}
