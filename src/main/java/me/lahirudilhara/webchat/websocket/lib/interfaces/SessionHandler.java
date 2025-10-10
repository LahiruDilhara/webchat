package me.lahirudilhara.webchat.websocket.lib.interfaces;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface SessionHandler {
    void onSessionConnect(WebSocketSession session,String username) throws Exception;
    void onSessionDisconnect(WebSocketSession session, String username) throws Exception;
    WebSocketSession getSessionById(String sessionId);
    List<WebSocketSession> getSessionsByUser(String username);
    Boolean isUserOnline(String username);
}
