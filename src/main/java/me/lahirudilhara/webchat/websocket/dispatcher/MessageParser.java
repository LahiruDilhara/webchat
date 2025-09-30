package me.lahirudilhara.webchat.websocket.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ValidationException;
import me.lahirudilhara.webchat.common.util.SchemaValidator;
import me.lahirudilhara.webchat.websocket.dto.requests.BaseRequestMessageDTO;
import me.lahirudilhara.webchat.websocket.lib.interfaces.IncomingMessageHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.SessionErrorHandler;
import me.lahirudilhara.webchat.websocket.lib.utils.JsonUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class MessageParser implements IncomingMessageHandler {
    private final SessionErrorHandler sessionErrorHandler;
    private final MessageDispatcher messageDispatcher;

    public MessageParser(SessionErrorHandler sessionErrorHandler, MessageDispatcher messageDispatcher) {
        this.sessionErrorHandler = sessionErrorHandler;
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public void handleMessage(WebSocketSession session, String message) throws Exception {
        BaseRequestMessageDTO baseRequestMessageDTO = null;
        try {
            baseRequestMessageDTO = JsonUtils.jsonToObject(message, BaseRequestMessageDTO.class);
        } catch (JsonProcessingException e) {
            sessionErrorHandler.sendErrorToSession(session, "Message in the wrong format");
            return;
        }
        if (baseRequestMessageDTO == null) {
            sessionErrorHandler.sendErrorToSession(session,"Message is empty");
            return;
        }
        try{
            SchemaValidator.validate(baseRequestMessageDTO);
        } catch (ValidationException e) {
            sessionErrorHandler.sendErrorToSession(session,e.getMessage());
            return;
        }
        messageDispatcher.dispatch(baseRequestMessageDTO,session.getPrincipal().getName(),session.getId());
    }
}
