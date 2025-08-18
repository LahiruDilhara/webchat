package me.lahirudilhara.webchat.service.websocket;

import me.lahirudilhara.webchat.entities.WebSocketUser;
import me.lahirudilhara.webchat.websocket.SessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@Service
public class WebSocketMessageService {
    private static final Logger log = LoggerFactory.getLogger(WebSocketMessageService.class);
    private final SessionRegistry sessionRegistry;

    public WebSocketMessageService(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void sendMessageToUser(String username,String message) {
        WebSocketUser user = sessionRegistry.getUser(username);
        if (user == null) {
            log.error("User not found. the user name is {}", username);
        }
        sendMessageToSession(message,user.getSession());
    }

    public void multicastToUsers(List<String> usernames,String message){
        List<WebSocketUser> users = sessionRegistry.findUsers(usernames);
        for (WebSocketUser user : users) {
            sendMessageToSession(message,user.getSession());
        }
    }

    private void sendMessageToSession(String message, WebSocketSession session){
        if(!session.isOpen()){
            log.warn("The session is closed. cannot send the message {}", message);
        }
        TextMessage textMessage = new TextMessage(message);
        try{
            session.sendMessage(textMessage);
        }
        catch (Exception e){
            log.error("Error while sending message to session. The message is {}", message);
        }
    }
}
