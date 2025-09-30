package me.lahirudilhara.webchat.websocket.services;

import me.lahirudilhara.webchat.websocket.lib.interfaces.RoomBroker;
import org.springframework.stereotype.Service;

@Service
public class SessionRoomValidator {

    private final RoomBroker roomBroker;

    public SessionRoomValidator(RoomBroker roomBroker) {
        this.roomBroker = roomBroker;
    }

    public boolean canSessionSendMessagesToRoom(String sessionId, Integer roomId){
        return roomBroker.isSessionInRoom(roomId, sessionId);
    }
}
