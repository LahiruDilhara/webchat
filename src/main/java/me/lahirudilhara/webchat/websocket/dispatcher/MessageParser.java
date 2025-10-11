package me.lahirudilhara.webchat.websocket.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.common.util.SchemaValidator;
import me.lahirudilhara.webchat.websocket.dto.requests.BaseRequestMessageDTO;
import me.lahirudilhara.webchat.websocket.interfaces.ClientErrorHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.IncomingMessageHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.SessionErrorHandler;
import me.lahirudilhara.webchat.websocket.lib.utils.JsonUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
public class MessageParser implements IncomingMessageHandler {
    private final SessionErrorHandler sessionErrorHandler;
    private final MessageDispatcher messageDispatcher;
    private final ClientErrorHandler clientErrorHandler;

    public MessageParser(SessionErrorHandler sessionErrorHandler, MessageDispatcher messageDispatcher, ClientErrorHandler clientErrorHandler) {
        this.sessionErrorHandler = sessionErrorHandler;
        this.messageDispatcher = messageDispatcher;
        this.clientErrorHandler = clientErrorHandler;
    }

    @Override
    public void handleMessage(WebSocketSession session, String message) throws Exception {
        log.info("Received message: {}", message);
        BaseRequestMessageDTO baseRequestMessageDTO = null;
        try {
            baseRequestMessageDTO = JsonUtils.jsonToObject(message, BaseRequestMessageDTO.class);
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage());
            sessionErrorHandler.sendErrorToSession(session, "Message in the wrong format");
            return;
        }
        if (baseRequestMessageDTO == null) {
            log.debug(String.format("Message in the wrong format: %s", message));
            sessionErrorHandler.sendErrorToSession(session,"Message is empty");
            return;
        }
        try{
            SchemaValidator.validate(baseRequestMessageDTO);
        } catch (ValidationException e) {
            log.debug(e.getMessage());
            clientErrorHandler.sendMessageErrorToSession(session.getId(),e.getMessage(), baseRequestMessageDTO.getUuid());
            return;
        }
        messageDispatcher.dispatch(baseRequestMessageDTO,session.getPrincipal().getName(),session.getId());
    }
}
