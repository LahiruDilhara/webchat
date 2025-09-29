package me.lahirudilhara.webchat.websocket.refactor.entities;

import java.util.List;

public record BroadcastData<T>(List<String> users, T data) {
}
