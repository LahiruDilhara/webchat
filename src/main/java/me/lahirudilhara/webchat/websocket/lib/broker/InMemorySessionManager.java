package me.lahirudilhara.webchat.websocket.lib.broker;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.lib.events.NewUserJoinedEvent;
import me.lahirudilhara.webchat.websocket.lib.events.UserDisconnectedEvent;
import me.lahirudilhara.webchat.websocket.lib.interfaces.RoomBroker;
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
    private final Map<String, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();
    private final Map<String,WebSocketSession> sessionsById = new ConcurrentHashMap<>();
    private final RoomBroker roomBroker;
    private final ApplicationEventPublisher applicationEventPublisher;

    public InMemorySessionManager(RoomBroker roomBroker, ApplicationEventPublisher applicationEventPublisher) {
        this.roomBroker = roomBroker;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onSessionConnect(WebSocketSession session) {
        String username = session.getPrincipal() != null ? session.getPrincipal().getName() : null;
        if (username == null) {
            log.warn("Session {} has no principal, skipping registration", session.getId());
            return;
        }
        if(!sessions.containsKey(username)) {
            sessions.put(username,new ArrayList<>());
        }
        if(!sessionsById.containsKey(session.getId())) {
            sessionsById.put(session.getId(),session);
        }
        if (!sessions.get(username).contains(session)) {
            sessions.get(username).add(session);
        }
        applicationEventPublisher.publishEvent(new NewUserJoinedEvent(username));
    }

    @Override
    public void onSessionDisconnect(WebSocketSession session) {
        String username = session.getPrincipal() != null ? session.getPrincipal().getName() : null;
        if (username == null) {
            log.warn("Session {} has no principal, skipping registration", session.getId());
            return;
        }
        if(!sessions.containsKey(username)) return;
        sessions.get(username).removeIf(c->c.getId().equals(session.getId()));
        if(sessions.get(username).isEmpty()) {
            sessions.remove(username);
        }
        sessionsById.remove(session.getId());
        roomBroker.removeFromAllRooms(session.getId());
        applicationEventPublisher.publishEvent(new UserDisconnectedEvent(username));
    }

    @Override
    public WebSocketSession getSessionById(String sessionId) {
        if(!sessionsById.containsKey(sessionId)) return null;
        return sessionsById.get(sessionId);
    }

    @Override
    public List<WebSocketSession> getSessionsByUser(String username) {
        if(!sessions.containsKey(username)) return null;
        return sessions.get(username);
    }

    @Override
    public Boolean isUserOnline(String username) {
        return sessions.containsKey(username);
    }
}
