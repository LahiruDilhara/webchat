package me.lahirudilhara.webchat.websocket.lib.interfaces;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface SessionHandler {
    void onSessionConnect(WebSocketSession session) throws Exception;
    void onSessionDisconnect(WebSocketSession session) throws Exception;
    WebSocketSession getSessionById(String sessionId);
    List<WebSocketSession> getSessionsByUser(String username);
}
