package me.lahirudilhara.webchat.websocket.lib.broker;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.dispatcher.SessionErrorHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageSender;
import me.lahirudilhara.webchat.websocket.lib.interfaces.RoomBroker;
import me.lahirudilhara.webchat.websocket.lib.interfaces.SessionHandler;
import me.lahirudilhara.webchat.websocket.lib.utils.JsonUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class MessageBrokerImpl implements MessageBroker {

    private final RoomBroker roomBroker;
    private final SessionHandler sessionHandler;
    private final MessageSender messageSender;

    public MessageBrokerImpl(RoomBroker roomBroker, SessionHandler sessionHandler, MessageSender messageSender) {
        this.roomBroker = roomBroker;
        this.sessionHandler = sessionHandler;
        this.messageSender = messageSender;
    }

    @Override
    public void sendMessageToRoom(Integer roomId, Object message) {
        List<String> sessionIds = roomBroker.getSessions(roomId);
        List<WebSocketSession> sessions = sessionIds.stream().map(sessionHandler::getSessionById).toList();

        String jsonMessage = objectToJson(message);
        if (jsonMessage == null) return;
        sessions.forEach(ws -> messageSender.sendMessage(jsonMessage, ws));
    }

    @Override
    public void sendMessageToUser(String username, Object message) {
        List<WebSocketSession> sessions = sessionHandler.getSessionsByUser(username);
        String jsonMessage = objectToJson(message);
        if (jsonMessage == null) return;
        sessions.forEach(ws -> messageSender.sendMessage(jsonMessage, ws));
    }

    @Override
    public void sendMessageToUserExceptSessions(String username, List<String> sessionIds, Object message) {
        List<WebSocketSession> sessions = sessionHandler.getSessionsByUser(username).stream().filter(ws-> !sessionIds.contains(ws.getId())).toList();
        String jsonMessage = objectToJson(message);
        if (jsonMessage == null) return;
        sessions.forEach(ws -> messageSender.sendMessage(jsonMessage, ws));
    }

    @Override
    public void sendMessageToRoomExceptUsers(Integer roomId, List<String> usernames, Object message) {
        List<String> sessionIds = roomBroker.getSessions(roomId);
        List<String> userSessionIds = usernames.stream().map(sessionHandler::getSessionsByUser).filter(Objects::nonNull).flatMap(List::stream).map(WebSocketSession::getId).toList();
        List<String> filteredSessions = sessionIds.stream().filter(sessionId -> !userSessionIds.contains(sessionId)).toList();
        List<WebSocketSession> sessions = filteredSessions.stream().map(sessionHandler::getSessionById).toList();
        String jsonMessage = objectToJson(message);
        if (jsonMessage == null) return;
        sessions.forEach(ws->messageSender.sendMessage(jsonMessage, ws));
    }

    @Override
    public void sendMessageToSession(String sessionId, Object message) {
        WebSocketSession session = sessionHandler.getSessionById(sessionId);
        if (session == null) return;
        String jsonMessage = objectToJson(message);
        if (jsonMessage == null) return;
        messageSender.sendMessage(jsonMessage, session);
    }

    private String objectToJson(Object data) {
        try {
            return JsonUtils.objectToJson(data);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to json", e);
        }
        return null;
    }
}
