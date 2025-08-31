package me.lahirudilhara.webchat.websocket.session;

import org.springframework.web.socket.WebSocketSession;

public interface SessionManager {

    public void onUserConnected(String username, WebSocketSession session);
    public void onUserDisconnected(String username);
    public WebSocketUserSession getUserByUsername(String username);
}
