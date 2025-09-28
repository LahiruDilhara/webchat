package me.lahirudilhara.webchat.websocket.lib.interfaces;

import org.springframework.web.socket.WebSocketSession;

public interface ConnectionLostHandler {
    void onConnectionLost(WebSocketSession session) throws Exception;
}
