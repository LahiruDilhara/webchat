package me.lahirudilhara.webchat.websocket.lib.events;

public record SessionErrorEvent(String sessionId, String message) {
}
