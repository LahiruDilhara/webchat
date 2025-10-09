package me.lahirudilhara.webchat.websocket.listners;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.dto.response.RoomUserLeftResponse;
import me.lahirudilhara.webchat.websocket.lib.broker.BrokerSession;
import me.lahirudilhara.webchat.websocket.lib.events.SessionDisconnectedEvent;
import me.lahirudilhara.webchat.websocket.lib.events.UserLeaveRoomEvent;
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

    public UserDisconnectRoomNotifierListener(RoomBroker roomBroker, MessageBroker messageBroker) {
        this.roomBroker = roomBroker;
        this.messageBroker = messageBroker;
    }

    @Async
    @EventListener(SessionDisconnectedEvent.class)
    public void handleSessionDisconnectedEvent(SessionDisconnectedEvent event) {
        List<Integer> sessionConnectedRooms = roomBroker.getSessionOnlineRooms(event.sessionId());


        sessionConnectedRooms.forEach(roomId -> {
            List<String> currentRoomUserAllSessions = roomBroker.getRoomConnectedUsersSessions(roomId, event.username());
            if(!currentRoomUserAllSessions.contains(event.sessionId())) {
                log.warn("Unexpected disconnect: user {} had no sessions but event triggered for {}", event.username(), event.sessionId());
                return;
            }
            // if this is the only session in that room, notify the room users
            if(currentRoomUserAllSessions.size() == 1){
                messageBroker.sendMessageToRoom(roomId, RoomUserLeftResponse.builder().uuid(null).roomId(roomId).username(event.username()).build());
                return;
            }
            roomBroker.removeSessionFromRoom(roomId, event.sessionId());
        });
    }
}
