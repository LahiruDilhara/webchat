package me.lahirudilhara.webchat.websocket.events;

public record SessionErrorEvent(String sessionId, String message) {
}
