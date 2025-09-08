package me.lahirudilhara.webchat.websocket.events;

import me.lahirudilhara.webchat.dto.wc.WebSocketError;

public record ClientErrorEvent(WebSocketError error, String username) {

}
