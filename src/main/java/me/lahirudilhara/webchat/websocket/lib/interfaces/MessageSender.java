package me.lahirudilhara.webchat.websocket.lib.interfaces;

import org.springframework.web.socket.WebSocketSession;

public interface MessageSender {
    void sendMessage(String message, WebSocketSession session);
}
