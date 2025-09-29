package me.lahirudilhara.webchat.websocket.interfaces;

public interface ClientErrorHandler {
    void sendMessageErrorToSession(String sessionId, String message, String uuid);
}
