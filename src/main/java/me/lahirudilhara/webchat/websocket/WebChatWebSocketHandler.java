package me.lahirudilhara.webchat.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

@Component
public class WebChatWebSocketHandler extends TextWebSocketHandler {
    private final SessionManager sessionManager;
    private final WebSocketExceptionHandler  webSocketExceptionHandler;

    public WebChatWebSocketHandler(SessionManager sessionManager, WebSocketExceptionHandler webSocketExceptionHandler) {
        this.sessionManager = sessionManager;
        this.webSocketExceptionHandler = webSocketExceptionHandler;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if(!session.isOpen()) return;
        if(Objects.requireNonNull(session.getPrincipal()).getName() == null) return;
        try{
            sessionManager.onUserConnect(session.getPrincipal().getName(),session);
        }
        catch(Exception e){
            webSocketExceptionHandler.handleWebSocketException(e,session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if(!session.isOpen()) return;
        if(Objects.requireNonNull(session.getPrincipal()).getName() == null) return;
        try{
            sessionManager.onUserMessage(message.getPayload(), session);
        }
        catch (Exception e){
            webSocketExceptionHandler.handleWebSocketException(e,session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if(Objects.requireNonNull(session.getPrincipal()).getName() == null) return;
        try{
            sessionManager.onUserDisconnect(session.getPrincipal().getName(),session);
        } catch (Exception e) {
            webSocketExceptionHandler.handleWebSocketException(e,session);
        }
    }
}
