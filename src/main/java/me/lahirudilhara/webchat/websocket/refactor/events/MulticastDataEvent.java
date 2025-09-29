package me.lahirudilhara.webchat.websocket.refactor.events;

import java.util.List;

public record MulticastDataEvent(List<String> users, Object data) {
}
