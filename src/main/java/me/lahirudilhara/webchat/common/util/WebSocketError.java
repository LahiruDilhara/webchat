package me.lahirudilhara.webchat.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.lahirudilhara.webchat.common.types.WebSocketErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
public class WebSocketError {
    private static final Logger log = LoggerFactory.getLogger(WebSocketError.class);

    public static void sendError(WebSocketSession session,String message){
        sendError(session,new WebSocketErrorResponse(message));
    }

    public static void sendError(WebSocketSession session, WebSocketErrorResponse data) {
        if(!session.isOpen()) return;
        try{
            String messageData = JsonUtil.objectToJson(data);
            session.sendMessage(new TextMessage(messageData));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
