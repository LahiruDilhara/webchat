package me.lahirudilhara.webchat.websocket.lib.interfaces;

import java.util.List;

public interface MessageBroker {
    void sendMessageToRoom(Integer roomId, Object message);
    void sendMessageToUser(String username, Object message);
    void sendMessageToUserExceptSessions(String username, List<String> sessionIds, Object message);
    void sendMessageToSession(String sessionId, Object message);
}
