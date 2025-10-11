package me.lahirudilhara.webchat.websocket.lib.events;

public record SessionLeaveRoomEvent(int roomId, String username, String sessionId) {
}
