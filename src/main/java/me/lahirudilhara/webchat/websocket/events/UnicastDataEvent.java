package me.lahirudilhara.webchat.websocket.events;

public class UnicastDataEvent {
    private final String username;
    private final Object object;

    public UnicastDataEvent(String username, Object object) {
        this.username = username;
        this.object = object;
    }

    public String getUsername() {
        return username;
    }

    public Object getObject() {
        return object;
    }
}
