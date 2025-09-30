package me.lahirudilhara.webchat.websocket.listners;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.dto.response.DeviceDisconnectedResponse;
import me.lahirudilhara.webchat.websocket.dto.response.RoomUserLeftResponse;
import me.lahirudilhara.webchat.websocket.lib.broker.BrokerSession;
import me.lahirudilhara.webchat.websocket.lib.events.SessionDisconnectedEvent;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import me.lahirudilhara.webchat.websocket.lib.interfaces.RoomBroker;
import me.lahirudilhara.webchat.websocket.lib.interfaces.SessionHandler;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDisconnectRoomNotifierListener {

    private final RoomBroker roomBroker;
    private final MessageBroker messageBroker;

    public UserDisconnectRoomNotifierListener(RoomBroker roomBroker,  MessageBroker messageBroker) {
        this.roomBroker = roomBroker;
        this.messageBroker = messageBroker;
    }

    @Async
    @EventListener(SessionDisconnectedEvent.class)
    public void handleSessionDisconnectedEvent(SessionDisconnectedEvent event) {
        List<Integer> roomIds = new ArrayList<>(roomBroker.getUserOnlineRooms(event.username()));
        roomIds.forEach(roomId -> {
            List<BrokerSession> userBrokerSessionsInRoom = roomBroker.getBrokerSessionsInRoom(roomId).stream().filter(bs->bs.username().equals(event.username())).toList();
            roomBroker.removeSessionFromRoom(roomId, event.sessionId());
            if(userBrokerSessionsInRoom.isEmpty()) {
                log.warn("Unexpected disconnect: user {} had no sessions but event triggered for {}", event.username(), event.sessionId());
                return;
            };
            if(userBrokerSessionsInRoom.size() == 1){
                messageBroker.sendMessageToRoom(roomId, RoomUserLeftResponse.builder().uuid(null).username(event.username()).build());
                return;
            }
            messageBroker.sendMessageToUserExceptSessions(event.username(),List.of(event.sessionId()), DeviceDisconnectedResponse.builder().uuid(null).build());
        });
    }
}
