package me.lahirudilhara.webchat.websocket.lib.interfaces;

import org.springframework.web.socket.WebSocketSession;

public interface MessageHandler {
    void handleMessage(WebSocketSession session, String message) throws Exception;
}
