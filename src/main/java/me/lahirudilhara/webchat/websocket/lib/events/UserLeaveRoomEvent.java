package me.lahirudilhara.webchat.websocket.lib.events;

public record UserLeaveRoomEvent(int roomId, String username) {
}
