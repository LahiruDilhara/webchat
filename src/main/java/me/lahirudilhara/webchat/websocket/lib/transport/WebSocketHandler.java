package me.lahirudilhara.webchat.websocket.lib.transport;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.lib.interfaces.ConnectionLostHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.IncomingMessageHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.SessionErrorHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final SessionHandler sessionHandler;
    private final IncomingMessageHandler incomingMessageHandler;
    private final SessionErrorHandler sessionErrorHandler;
    private final ConnectionLostHandler  connectionLostHandler;

    public WebSocketHandler(@Autowired(required = false) SessionHandler sessionHandler, @Autowired(required = false) IncomingMessageHandler incomingMessageHandler, @Autowired(required = false) SessionErrorHandler sessionErrorHandler, @Autowired(required = false) ConnectionLostHandler connectionLostHandler) {
        this.sessionHandler = sessionHandler;
        this.incomingMessageHandler = incomingMessageHandler;
        this.sessionErrorHandler = sessionErrorHandler;
        this.connectionLostHandler = connectionLostHandler;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        if (sessionHandler == null) return;
        if (session.getPrincipal() == null || session.getPrincipal().getName() == null) {
            handleUnAuthorized(session);
            return;
        }
        try{
            sessionHandler.onSessionConnect(session,session.getPrincipal().getName());
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        if (sessionHandler == null || incomingMessageHandler == null) return;
        if (session.getPrincipal() == null || session.getPrincipal().getName() == null) {
            handleUnAuthorized(session);
            return;
        }
        try{
            incomingMessageHandler.handleMessage(session,message.getPayload());
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (sessionHandler == null) return;
        log.error("Transport error occurred. Session: {}", session.getId(), exception);
        if(connectionLostHandler != null){
            try{
                connectionLostHandler.onConnectionLost(session);
            }
            catch (Exception e){
                log.error(e.getMessage(), e);
            }
        }
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        if (sessionHandler == null) return;
        if (session.getPrincipal() == null || session.getPrincipal().getName() == null) {
            handleUnAuthorized(session);
            return;
        }
        try{
            sessionHandler.onSessionDisconnect(session,session.getPrincipal().getName());
            super.afterConnectionClosed(session,status);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void handleUnAuthorized(WebSocketSession session) {
        if (sessionErrorHandler == null) return;
        try {
            sessionErrorHandler.sendErrorToSession(session, "Unauthorized");
        } catch (Exception e) {
            log.error("Error while sending error to SessionHandler", e);
        }
    }
}
