package me.lahirudilhara.webchat.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SessionManager {
    private final SessionRegistry sessionRegistry;
    public SessionManager(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void onUserConnect(String username, WebSocketSession session){
        this.sessionRegistry.addUser(username, session);
    }

    public void onUserDisconnect(String username, WebSocketSession session){
        this.sessionRegistry.findAndRemoveUser(username);
    }

    public void onUserMessage(String message, WebSocketSession session){

    }
}
