package me.lahirudilhara.webchat.websocket.events;

import java.util.List;

public class MulticastDataEvent {
    private final List<String> users;
    private final Object data;

    public MulticastDataEvent(List<String> users, Object data) {
        this.users = users;
        this.data = data;
    }

    public List<String> getUsers() {
        return users;
    }

    public Object getData() {
        return data;
    }
}
