package me.lahirudilhara.webchat.websocket.lib.interfaces;

import org.springframework.web.socket.WebSocketSession;

public interface SessionErrorHandler {
    void sendErrorToSession(WebSocketSession session, String message) ;
    void sendErrorToSession(String sessionId, String message) ;
}
