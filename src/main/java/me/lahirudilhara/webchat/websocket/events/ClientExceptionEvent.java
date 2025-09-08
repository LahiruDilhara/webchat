package me.lahirudilhara.webchat.websocket.events;

public record ClientExceptionEvent(String username, String message) {
}
