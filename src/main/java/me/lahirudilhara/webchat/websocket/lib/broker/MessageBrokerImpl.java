package me.lahirudilhara.webchat.websocket.lib.broker;

import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageSender;
import me.lahirudilhara.webchat.websocket.lib.interfaces.RoomBroker;
import me.lahirudilhara.webchat.websocket.lib.interfaces.SessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

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
    public void sendMessageToRoom(Integer roomId, String message) {
        List<String> sessionIds = roomBroker.getSessions(roomId);
        List<WebSocketSession> sessions = sessionIds.stream().map(sessionHandler::getSessionById).toList();
        sessions.forEach(ws->messageSender.sendMessage(message,ws));
    }

    @Override
    public void sendMessageToUser(String username, String message) {
        List<WebSocketSession> sessions= sessionHandler.getSessionsByUser(username);
        sessions.forEach(ws->messageSender.sendMessage(message,ws));
    }
}
