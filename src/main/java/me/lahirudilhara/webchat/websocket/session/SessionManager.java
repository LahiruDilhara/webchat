package me.lahirudilhara.webchat.websocket.session;

import org.springframework.web.socket.WebSocketSession;

public interface SessionManager {

    void onUserConnected(String username, WebSocketSession session);
    void onUserDisconnected(String username);
    WebSocketUserSession getUserByUsername(String username);
}
