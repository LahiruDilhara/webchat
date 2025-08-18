package me.lahirudilhara.webchat.websocket;

import me.lahirudilhara.webchat.entities.WebSocketUserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private static final Logger log = LoggerFactory.getLogger(SessionManager.class);


    private final Map<String, WebSocketUserSession> webSocketUsers;

    public SessionManager() {
        webSocketUsers = new ConcurrentHashMap<>();

    }

    public void addWebSocketSession(WebSocketSession session) {
        WebSocketUserSession webSocketUserSession = new WebSocketUserSession(session.getPrincipal().getName(), Instant.now(),session);
        webSocketUsers.put(session.getPrincipal().getName(), webSocketUserSession);
    }

    public void removeUser(String username){
        this.webSocketUsers.remove(username);
    }

    public boolean isUserOnline(String username){
        WebSocketUserSession userSession = this.webSocketUsers.get(username);
        if(userSession == null){
            return false;
        }
        if(!userSession.getSession().isOpen()){
            return false;
        }
        return true;
    }

    public WebSocketUserSession getUserByUsername(String username){
        return this.webSocketUsers.get(username);
    }

    public List<String> getActiveUsers(){
        return new ArrayList<>(this.webSocketUsers.keySet());
    }

    public void sendMessageToSession(String username, String message){
        WebSocketUserSession webSocketUserSession = this.webSocketUsers.get(username);
        if(webSocketUserSession == null){
            return;
        }
        if(!webSocketUserSession.getSession().isOpen()){
            log.error("Session has been closed for the user {}",username);
            return;
        }
        try{
            webSocketUserSession.getSession().sendMessage(new TextMessage(message));
        }
        catch(Exception e){
            log.error("Error while sending data to session. the user name is {}",username, e);
        }
    }
}
