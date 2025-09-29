package me.lahirudilhara.webchat.websocket.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.common.util.JsonUtil;
import me.lahirudilhara.webchat.websocket.dto.WebSocketError;
import me.lahirudilhara.webchat.websocket.dto.response.ClientErrorMessageResponse;
import me.lahirudilhara.webchat.websocket.interfaces.ClientErrorHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageSender;
import me.lahirudilhara.webchat.websocket.lib.interfaces.SessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class SessionErrorHandler implements me.lahirudilhara.webchat.websocket.lib.interfaces.SessionErrorHandler, ClientErrorHandler {
    private final MessageSender messageSender;
    private final SessionHandler sessionHandler;

    public SessionErrorHandler(MessageSender messageSender, SessionHandler sessionHandler) {
        this.messageSender = messageSender;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void sendErrorToSession(WebSocketSession session, String message) {
        WebSocketError webSocketError = new WebSocketError(message);
        sendError(session, webSocketError);
    }

    @Override
    public void sendErrorToSession(String sessionId, String message) {
        WebSocketSession session =  sessionHandler.getSessionById(sessionId);
        if(session != null) {
            sendErrorToSession(session, message);
        }
    }

    @Override
    public void sendMessageErrorToSession(String sessionId, String message, String uuid) {
        WebSocketSession session =  sessionHandler.getSessionById(sessionId);
        if(session == null) return;
        ClientErrorMessageResponse clientErrorMessageResponse = ClientErrorMessageResponse.builder().uuid(uuid).error(message).build();
        sendError(session, clientErrorMessageResponse);
    }

    private void sendError(WebSocketSession session, Object data) {
        try{
            String json = JsonUtil.objectToJson(data);
            messageSender.sendMessage(json, session);
        } catch (JsonProcessingException e) {
            log.error("Error converting WebSocketError to JSON", e);
        }
    }
}
