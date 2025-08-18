package me.lahirudilhara.webchat.websocket;

import me.lahirudilhara.webchat.entities.WebSocketUserSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
public class SessionRegistry {

    // Use a ConcurrentHashMap for thread-safe access
    private final Map<String, WebSocketUserSession> webSocketUsers;

    public SessionRegistry() {
        webSocketUsers = new ConcurrentHashMap<>();
    }

    public void addUser(String username, WebSocketSession session) {
        WebSocketUserSession user = new WebSocketUserSession(username, Instant.now(), session);
        webSocketUsers.put(username, user);
    }

    public WebSocketUserSession findAndRemoveUser(String username) {
        return webSocketUsers.remove(username); // remove and return if exists, null otherwise
    }

    public boolean isUserExists(String username) {
        return webSocketUsers.containsKey(username);
    }

    public List<WebSocketUserSession> getUsers() {
        return List.copyOf(webSocketUsers.values());
    }

    public WebSocketUserSession getUser(String username) {
        return webSocketUsers.get(username);
    }

    public WebSocketUserSession getUsersByUsername(String username) {
        return webSocketUsers.get(username); // Same as getUser, consider removing this duplicate
    }

    public List<WebSocketUserSession> findUsers(List<String> usernames) {
        return usernames.stream()
                .map(webSocketUsers::get)
                .filter(user -> user != null)
                .collect(Collectors.toList());
    }
}