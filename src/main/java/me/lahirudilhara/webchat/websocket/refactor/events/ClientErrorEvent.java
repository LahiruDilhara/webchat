package me.lahirudilhara.webchat.websocket.refactor.events;

import me.lahirudilhara.webchat.websocket.dto.WebSocketError;

public record ClientErrorEvent(WebSocketError error, String username,String sessionId) {

}
