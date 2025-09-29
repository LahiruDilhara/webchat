package me.lahirudilhara.webchat.websocket.lib.interfaces;

import java.util.List;

public interface RoomBroker {
    void addSessionToRoom(Integer roomId, String sessionId);
    void removeSessionFromRoom(Integer roomId, String sessionId);
    boolean isRoomExists(Integer roomId);
    List<String> getSessions(Integer roomId);
}
