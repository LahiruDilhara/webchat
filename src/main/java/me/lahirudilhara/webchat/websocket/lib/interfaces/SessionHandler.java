package me.lahirudilhara.webchat.websocket.lib.interfaces;

import org.springframework.web.socket.WebSocketSession;

import java.net.http.WebSocket;

public interface SessionHandler {
    void onSessionConnect(WebSocketSession session) throws Exception;
    void onSessionDisconnect(WebSocketSession session) throws Exception;
}
