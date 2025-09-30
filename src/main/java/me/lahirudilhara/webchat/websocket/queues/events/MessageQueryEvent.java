package me.lahirudilhara.webchat.websocket.queues.events;

import me.lahirudilhara.webchat.websocket.dto.requests.RemainMessageDTO;

public record MessageQueryEvent(RemainMessageDTO remainMessageDTO, String sender, String sessionId) {
}
