package me.lahirudilhara.webchat.websocket.listeners;

import me.lahirudilhara.webchat.websocket.events.NewClientJoinedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public class SessionRegisterListener {

    @Async
    @EventListener
    public void OnNewClientJoinedEvent(NewClientJoinedEvent newClientJoinedEvent) {

    }
}
