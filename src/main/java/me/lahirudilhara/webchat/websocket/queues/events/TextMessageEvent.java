package me.lahirudilhara.webchat.websocket.queues.events;

import me.lahirudilhara.webchat.websocket.dto.requests.TextMessageDTO;

public record TextMessageEvent(TextMessageDTO message,String senderUsername, String sessionId) {
}
