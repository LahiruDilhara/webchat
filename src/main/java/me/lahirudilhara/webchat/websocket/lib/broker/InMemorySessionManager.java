package me.lahirudilhara.webchat.websocket.lib.broker;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.lib.events.SessionConnectedEvent;
import me.lahirudilhara.webchat.websocket.lib.events.SessionDisconnectedEvent;
import me.lahirudilhara.webchat.websocket.lib.interfaces.SessionHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class InMemorySessionManager implements SessionHandler {
    private final Map<String, List<WebSocketSession>> usernameToSessions = new ConcurrentHashMap<>();
    private final Map<String,WebSocketSession> sessionsBySessionId = new ConcurrentHashMap<>();

    private final ApplicationEventPublisher applicationEventPublisher;

    public InMemorySessionManager( ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onSessionConnect(WebSocketSession session,String username) {
        if (username == null) {
            log.warn("Session {} has no principal, skipping registration", session.getId());
            return;
        }
        if(!usernameToSessions.containsKey(username)) {
            usernameToSessions.put(username,new ArrayList<>());
        }
        List<String> otherSessionIds = usernameToSessions.get(username).stream().map(WebSocketSession::getId).toList();
        if(!sessionsBySessionId.containsKey(session.getId())) {
            sessionsBySessionId.put(session.getId(),session);
        }
        if (!usernameToSessions.get(username).contains(session)) {
            usernameToSessions.get(username).add(session);
        }
        log.debug("Session {} connected", session.getId());
        applicationEventPublisher.publishEvent(new SessionConnectedEvent(username,session.getId(),otherSessionIds));
    }

    private void removeSessionFromUsernameToSessions(String username, String sessionId){
        if(!usernameToSessions.containsKey(username)) return;
        List<String> otherSessionIds = usernameToSessions.get(username).stream().map(WebSocketSession::getId).toList();
        usernameToSessions.get(username).removeIf(ws->ws.getId().equals(sessionId));
        applicationEventPublisher.publishEvent(new SessionDisconnectedEvent(username,sessionId,otherSessionIds));
    }

    private void removeSessionFromSessionsBySessionId(String sessionId){
        if(!sessionsBySessionId.containsKey(sessionId)) return;
        sessionsBySessionId.remove(sessionId);
    }

    @Override
    public void onSessionDisconnect(WebSocketSession session, String username) {
        if (username == null) {
            log.warn("Session {} has no principal, skipping registration", session.getId());
            return;
        }
        removeSessionFromUsernameToSessions(username,session.getId());
        removeSessionFromSessionsBySessionId(session.getId());
        log.debug("Session {} disconnected", session.getId());
    }

    @Override
    public WebSocketSession getSessionById(String sessionId) {
        if(!sessionsBySessionId.containsKey(sessionId)) return null;
        return sessionsBySessionId.get(sessionId);
    }

    @Override
    public List<WebSocketSession> getSessionsByUser(String username) {
        if(!usernameToSessions.containsKey(username)) return new ArrayList<>();
        return usernameToSessions.get(username);
    }

    @Override
    public Boolean isUserOnline(String username) {
        return usernameToSessions.containsKey(username);
    }
}
