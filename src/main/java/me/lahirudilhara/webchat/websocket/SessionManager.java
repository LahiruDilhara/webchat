package me.lahirudilhara.webchat.websocket;

import me.lahirudilhara.webchat.entities.WebSocketUserSession;
import org.springframework.web.socket.WebSocketSession;

public interface SessionManager {

    public void onUserConnected(String username, WebSocketSession session);
    public void onUserDisconnected(String username);
    public WebSocketUserSession getUserByUsername(String username);
}
