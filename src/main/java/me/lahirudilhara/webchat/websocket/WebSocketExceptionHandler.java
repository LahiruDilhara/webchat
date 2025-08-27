package me.lahirudilhara.webchat.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import jakarta.validation.ValidationException;
import me.lahirudilhara.webchat.core.exceptions.BaseWebSocketException;
import me.lahirudilhara.webchat.core.lib.WebSocketErrorResponse;
import me.lahirudilhara.webchat.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
public class WebSocketExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(WebSocketExceptionHandler.class);

    public void handleWebSocketException(Exception exception,WebSocketSession session) {
        log.warn("Exception caught in WebSocketExceptionHandler {}", exception.getMessage());
        if(exception instanceof ValidationException){
            handleSchemaValidationExceptions((ValidationException) exception, session);
            return;
        }
        if(exception instanceof BaseWebSocketException){
            handleBaseExceptions((BaseWebSocketException) exception, session);
            return;
        }
        exception.printStackTrace();
        handleAllExceptions(session);
    }

    private void handleSchemaValidationExceptions(ValidationException ex, WebSocketSession session) {
        WebSocketErrorResponse response = new WebSocketErrorResponse(ex.getMessage());
        sendError(session, response);
    }

    private void handleBaseExceptions(BaseWebSocketException ex, WebSocketSession session) {
        WebSocketErrorResponse response = new WebSocketErrorResponse(ex.getMessage());
        sendError(session, response);
    }

    private void handleAllExceptions(WebSocketSession session) {
        String message = "Unexpected error occurred";
        WebSocketErrorResponse response = new WebSocketErrorResponse(message);
        sendError(session, response);
    }

    public void sendError(WebSocketSession session, WebSocketErrorResponse data) {
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
