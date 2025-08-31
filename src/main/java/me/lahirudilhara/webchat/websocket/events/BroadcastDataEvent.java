package me.lahirudilhara.webchat.websocket.events;

import me.lahirudilhara.webchat.websocket.entities.BroadcastData;

public class BroadcastDataEvent {
    private final BroadcastData data;

    public BroadcastDataEvent(BroadcastData data) {
        this.data = data;
    }

    public BroadcastData getData() {
        return data;
    }
}
