package me.lahirudilhara.webchat.websocket.refactor.session;

import me.lahirudilhara.webchat.websocket.refactor.events.ClientConnectedEvent;
import me.lahirudilhara.webchat.websocket.refactor.events.ClientDisconnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySessionManager implements SessionManager{
    private static final Logger log = LoggerFactory.getLogger(InMemorySessionManager.class);

    private final Map<String, List<WebSocketUserSession>> webSocketUsers;
    private final ApplicationEventPublisher applicationEventPublisher;

    public InMemorySessionManager(ApplicationEventPublisher applicationEventPublisher) {
        webSocketUsers = new ConcurrentHashMap<>();
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void onUserConnection(String username, WebSocketSession session){
        WebSocketUserSession webSocketUserSession = new WebSocketUserSession(username,Instant.now(),session);
        if(!webSocketUsers.containsKey(username)){
            webSocketUsers.put(username,new ArrayList<>());
            applicationEventPublisher.publishEvent(new ClientConnectedEvent(session.getPrincipal().getName(),session));
        }
        webSocketUsers.get(username).add(webSocketUserSession);
        log.info("A new User connected with username {} and session id {}",username,session.getId());
    }

    public void onUserDisconnection(String username, String sessionId){
        if(!webSocketUsers.containsKey(username)) return;
        List<WebSocketUserSession> webSocketUserSessions = webSocketUsers.get(username);
        WebSocketUserSession webSocketUserSession = webSocketUserSessions.stream().filter(c->c.getSession().getId().equals(sessionId)).findFirst().orElse(null);
        if(webSocketUserSession == null) return;
        webSocketUsers.get(username).remove(webSocketUserSession);
        if(webSocketUserSessions.isEmpty()) {
            webSocketUsers.remove(username);
            applicationEventPublisher.publishEvent(new ClientDisconnectedEvent(username));
        }
        log.info("A new User Disconnected with username {} and session id {}",username,sessionId);
    }

    @Override
    public List<WebSocketUserSession> getUserSessions(String username) {
        if(!webSocketUsers.containsKey(username)) return new ArrayList<>();
        return webSocketUsers.get(username);
    }

    @Override
    public List<WebSocketUserSession> getMultipleUserSessions(List<String> usernames) {
        List<WebSocketUserSession> webSocketUserSessions = new ArrayList<>();
        for(String username : usernames){
            webSocketUserSessions.addAll(getUserSessions(username));
        }
        return webSocketUserSessions;
    }
}
