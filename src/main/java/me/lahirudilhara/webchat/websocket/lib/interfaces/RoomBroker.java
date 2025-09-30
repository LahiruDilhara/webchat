package me.lahirudilhara.webchat.websocket.lib.interfaces;

import me.lahirudilhara.webchat.websocket.lib.broker.BrokerSession;

import java.util.List;

public interface RoomBroker {
    void addSessionToRoom(Integer roomId, String sessionId, String username);
    void removeSessionFromRoom(Integer roomId, String sessionId);
    boolean isRoomExists(Integer roomId);
    List<String> getSessions(Integer roomId);
    boolean isUserInRoom(Integer roomId, String username);
    void removeFromAllRooms(String sessionId);
    boolean isSessionInRoom(Integer roomId, String sessionId);
    List<Integer> getUserOnlineRooms(String username);
    List<BrokerSession> getBrokerSessionsInRoom(Integer roomId);
}
