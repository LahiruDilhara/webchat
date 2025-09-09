package me.lahirudilhara.webchat.websocket.session;

import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.List;

public interface SessionManager {

    void onUserConnected(String username, int roomId, WebSocketSession session, Instant joinedAt);
    void onUserDisconnected(String username, WebSocketSession session);
    WebSocketUserSession getUserByUsername(String username);
    List<WebSocketUserSession> getUsersByUsernames(List<String> usernames);
}
