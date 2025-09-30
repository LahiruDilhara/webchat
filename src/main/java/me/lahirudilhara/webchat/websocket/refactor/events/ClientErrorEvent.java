package me.lahirudilhara.webchat.websocket.refactor.events;

import me.lahirudilhara.webchat.websocket.dto.response.WebSocketError;

public record ClientErrorEvent(WebSocketError error, String username,String sessionId) {

}
