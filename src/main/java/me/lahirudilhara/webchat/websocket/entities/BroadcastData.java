package me.lahirudilhara.webchat.websocket.entities;

import java.util.List;

public record BroadcastData<T>(List<String> users, T data) {
}
