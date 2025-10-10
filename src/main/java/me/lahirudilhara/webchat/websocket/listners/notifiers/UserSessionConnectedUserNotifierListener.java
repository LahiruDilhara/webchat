package me.lahirudilhara.webchat.websocket.listners.notifiers;

import me.lahirudilhara.webchat.websocket.dto.response.NewDeviceConnectedWithRoomResponse;
import me.lahirudilhara.webchat.websocket.lib.events.SessionConnectedEvent;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UserSessionConnectedUserNotifierListener {

    private final MessageBroker messageBroker;

    public UserSessionConnectedUserNotifierListener(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @Async
    @EventListener(SessionConnectedEvent.class)
    public void onUserSessionConnectedEvent(SessionConnectedEvent event) {
        event.otherSessions().forEach(sessionId -> {
            messageBroker.sendMessageToSession(sessionId, NewDeviceConnectedWithRoomResponse.builder().uuid(null).build());
        });
    }
}
