package me.lahirudilhara.webchat.websocket.refactor.session;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface SessionManager {

    void onUserConnection(String username, WebSocketSession session);
    void onUserDisconnection(String username, String sessionId);
    List<WebSocketUserSession> getUserSessions(String username);
    List<WebSocketUserSession> getMultipleUserSessions(List<String> usernames);
}
