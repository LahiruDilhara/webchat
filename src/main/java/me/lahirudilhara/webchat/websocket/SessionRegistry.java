package me.lahirudilhara.webchat.websocket;

import me.lahirudilhara.webchat.entities.WebSocketUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("singleton")
public class SessionRegistry {
    private List<WebSocketUser> webSocketUsers;

    public SessionRegistry() {
        webSocketUsers = new ArrayList<>();
    }

    public void addUser(String username, WebSocketSession session) {
        webSocketUsers.add(new WebSocketUser(username, Instant.now(),session));
    }

    public WebSocketUser findAndRemoveUser(String username) {
        WebSocketUser user = null;
        user = webSocketUsers.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
        if (user != null) {
            webSocketUsers.remove(user);
        }
        return user;
    }
    public boolean isUserExists(String username){
        return webSocketUsers.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public List<WebSocketUser> getUsers() {
        return  webSocketUsers;
    }

    public WebSocketUser getUser(String username){
        return  webSocketUsers.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    public WebSocketUser getUsersByUsername(String username){
        return webSocketUsers.stream().filter(u->u.getUsername().equals(username)).findFirst().orElse(null);
    }

    public List<WebSocketUser> findUsers(List<String> usernames){
        return webSocketUsers.stream().filter(u->usernames.contains(u.getUsername())).toList();
    }
}
