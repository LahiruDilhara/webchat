package me.lahirudilhara.webchat.websocket.lib.events;

import java.util.List;

public record SessionConnectedEvent(String username, String sessionId, List<String> otherSessions) {
}
