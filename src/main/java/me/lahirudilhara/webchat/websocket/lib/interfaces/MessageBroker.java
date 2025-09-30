package me.lahirudilhara.webchat.websocket.lib.interfaces;

public interface MessageBroker {
    void sendMessageToRoom(Integer roomId, Object message);
    void sendMessageToUser(String username, Object message);
    void sendMessageToSession(String sessionId, Object message);
}
