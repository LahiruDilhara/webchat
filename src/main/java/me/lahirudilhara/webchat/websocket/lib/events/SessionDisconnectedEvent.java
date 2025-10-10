package me.lahirudilhara.webchat.websocket.lib.events;

import java.util.List;

public record SessionDisconnectedEvent(String username, String sessionId, List<String> otherSessionIds) {
}
