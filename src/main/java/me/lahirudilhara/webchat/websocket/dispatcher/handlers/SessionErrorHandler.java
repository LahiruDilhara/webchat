package me.lahirudilhara.webchat.websocket.dispatcher.handlers;

import me.lahirudilhara.webchat.common.util.JsonUtil;
import me.lahirudilhara.webchat.websocket.dto.WebSocketError;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageSender;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SessionErrorHandler implements me.lahirudilhara.webchat.websocket.lib.interfaces.SessionErrorHandler {
    private final MessageSender messageSender;

    public SessionErrorHandler(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void sendErrorToSession(WebSocketSession session, String message) throws Exception {
        WebSocketError webSocketError = new WebSocketError(message);
        String json = JsonUtil.objectToJson(webSocketError);
        messageSender.sendMessage(json, session);
    }
}
