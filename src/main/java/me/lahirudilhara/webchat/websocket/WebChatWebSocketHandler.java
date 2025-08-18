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
    private final WebChatController webChatController;
    private final WebSocketExceptionHandler  webSocketExceptionHandler;

    public WebChatWebSocketHandler(SessionManager sessionManager, WebSocketExceptionHandler webSocketExceptionHandler,WebChatController webChatController) {
        this.sessionManager = sessionManager;
        this.webSocketExceptionHandler = webSocketExceptionHandler;
        this.webChatController = webChatController;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if(!session.isOpen()) return;
        if(Objects.requireNonNull(session.getPrincipal()).getName() == null) return;
        sessionManager.addWebSocketSession(session);
//        try{
//            sessionManager.onUserConnect(session.getPrincipal().getName(),session);
//        }
//        catch(Exception e){
//            webSocketExceptionHandler.handleWebSocketException(e,session);
//        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if(!session.isOpen()) return;
        if(Objects.requireNonNull(session.getPrincipal()).getName() == null) return;
        try{
            webChatController.onMessage(message.getPayload(),session.getPrincipal().getName());
        }
        catch (Exception e){
            webSocketExceptionHandler.handleWebSocketException(e,session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if(Objects.requireNonNull(session.getPrincipal()).getName() == null) return;
        sessionManager.removeUser(session.getPrincipal().getName());
//        try{
//            sessionManager.onUserDisconnect(session.getPrincipal().getName(),session);
//        } catch (Exception e) {
//            webSocketExceptionHandler.handleWebSocketException(e,session);
//        }
    }
}
