package me.lahirudilhara.webchat.websocket.listners.notifiers;

import me.lahirudilhara.webchat.websocket.dto.response.NewRoomUserResponse;
import me.lahirudilhara.webchat.websocket.lib.events.UserJoinedRoomEvent;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UserJoinedRoomNotifierListener {

    private final MessageBroker messageBroker;

    public UserJoinedRoomNotifierListener(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @Async
    @EventListener(UserJoinedRoomEvent.class)
    public void onUserJoinedRoomEvent(UserJoinedRoomEvent event) {
        messageBroker.sendMessageToRoom(event.roomId(),NewRoomUserResponse.builder().roomId(event.roomId()).uuid(null).username(event.username()).build());
    }
}
