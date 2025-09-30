package me.lahirudilhara.webchat.websocket.listners.events;

public record SessionErrorEvent(String sessionId, String message) {
}
