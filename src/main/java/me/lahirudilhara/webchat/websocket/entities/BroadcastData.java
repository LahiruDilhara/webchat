package me.lahirudilhara.webchat.websocket.entities;

import java.util.List;

public class BroadcastData {
    private final List<String> users;
    private final Object data;

    public BroadcastData(List<String> users, Object data) {
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
