package me.lahirudilhara.webchat.websocket;

import me.lahirudilhara.webchat.core.exceptions.BaseWebSocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(WebSocketExceptionHandler.class);

    public void handleWebSocketException(Exception exception,WebSocketSession session) {
        log.warn("Exception caught in WebSocketExceptionHandler {}", exception.getMessage());
        handleAllExceptions(session);
    }

    private void handleAllExceptions(WebSocketSession session) {
        String message = "Unexpected error occurred";
        sendError(session, message);
    }

    private void sendError(WebSocketSession session, String message) {
        if(!session.isOpen()) return;
        try{
            session.sendMessage(new TextMessage(message));
        }
        catch(Exception ex){
            log.error(ex.getMessage(), ex);
        }
    }
}
