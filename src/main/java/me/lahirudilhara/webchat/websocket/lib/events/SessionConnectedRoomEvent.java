package me.lahirudilhara.webchat.websocket.lib.events;

public record SessionConnectedRoomEvent(int roomId, String username, String sessionId) {
}
