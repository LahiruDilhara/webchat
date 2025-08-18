package me.lahirudilhara.webchat.service.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.lahirudilhara.webchat.core.exceptions.BaseWebSocketException;
import me.lahirudilhara.webchat.core.util.JsonUtil;
import me.lahirudilhara.webchat.websocket.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketMessageHandler {
    private static final Logger log = LoggerFactory.getLogger(WebSocketMessageHandler.class);
    private final SessionManager sessionManager;

    public WebSocketMessageHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void sendDataIfOnline(String username,Object data) {
        try {
            String jsonString = JsonUtil.objectToJson(data);
            sessionManager.sendMessageToSession(username, jsonString);
        } catch (JsonProcessingException e) {
            log.error("Json processing error", e);
            throw new BaseWebSocketException("Internal server error");
        }
    }

    public void multicastDataToOnlineUsers(List<String> usernames,Object data){
        usernames.forEach(name -> sendDataIfOnline(name,data));
    }
}
