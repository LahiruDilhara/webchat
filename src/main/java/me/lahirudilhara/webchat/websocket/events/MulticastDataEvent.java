package me.lahirudilhara.webchat.websocket.events;

import java.util.List;

public record MulticastDataEvent(List<String> users, Object data) {
}
