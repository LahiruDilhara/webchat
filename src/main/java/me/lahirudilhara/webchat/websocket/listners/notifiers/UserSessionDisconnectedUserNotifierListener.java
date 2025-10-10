package me.lahirudilhara.webchat.websocket.listners.notifiers;

import me.lahirudilhara.webchat.websocket.dto.response.DeviceDisconnectedResponse;
import me.lahirudilhara.webchat.websocket.lib.events.SessionDisconnectedEvent;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UserSessionDisconnectedUserNotifierListener {

    private final MessageBroker messageBroker;

    public UserSessionDisconnectedUserNotifierListener(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @Async
    @EventListener(SessionDisconnectedEvent.class)
    public void onSessionDisconnected(SessionDisconnectedEvent event) {
        event.otherSessionIds().forEach(sessionId -> {
           messageBroker.sendMessageToSession(sessionId, DeviceDisconnectedResponse.builder().uuid(null).build());
        });
    }
}
