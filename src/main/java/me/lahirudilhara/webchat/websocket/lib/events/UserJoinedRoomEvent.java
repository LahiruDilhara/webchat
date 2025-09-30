package me.lahirudilhara.webchat.websocket.lib.events;

public record UserJoinedRoomEvent(int roomId, String username) {
}
