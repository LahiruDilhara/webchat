package me.lahirudilhara.webchat.websocket.lib.interfaces;

public interface MessageBroker {
    void sendMessageToRoom(Integer roomId, String message);
    void sendMessageToUser(String username, String message);
}
