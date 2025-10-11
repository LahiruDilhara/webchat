package me.lahirudilhara.webchat.websocket.listners.notifiers;

import me.lahirudilhara.webchat.websocket.dto.response.RoomUserLeftResponse;
import me.lahirudilhara.webchat.websocket.lib.events.UserLeaveRoomEvent;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLeftRoomNotifierListener {

    private final MessageBroker messageBroker;

    public UserLeftRoomNotifierListener(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    @Async
    @EventListener
    public void onUserLeftRoomEvent(UserLeaveRoomEvent userLeaveRoomEvent) {
        messageBroker.sendMessageToRoomExceptUsers(userLeaveRoomEvent.roomId(), List.of(userLeaveRoomEvent.username()), RoomUserLeftResponse.builder().uuid(null).roomId(userLeaveRoomEvent.roomId()).username(userLeaveRoomEvent.username()).build());
    }
}
