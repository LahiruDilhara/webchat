package me.lahirudilhara.webchat.websocket.session;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface SessionManager {

    void onUserConnected(String username, WebSocketSession session);
    void onUserDisconnected(String username);
    WebSocketUserSession getUserByUsername(String username);
    List<WebSocketUserSession> getUsersByUsernames(List<String> usernames);
}
