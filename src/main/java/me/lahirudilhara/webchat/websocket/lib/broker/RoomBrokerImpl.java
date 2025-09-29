package me.lahirudilhara.webchat.websocket.lib.broker;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.lib.interfaces.RoomBroker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RoomBrokerImpl implements RoomBroker {
    private final Map<Integer, List<BrokerSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void addSessionToRoom(Integer roomId, String sessionId, String username) {
        if (!rooms.containsKey(roomId)) {
            rooms.put(roomId, new ArrayList<>());
        }
        log.debug("Adding session to room {} with sessionId {}", roomId, sessionId);
        rooms.get(roomId).add(new BrokerSession( username, sessionId));
    }

    @Override
    public void removeSessionFromRoom(Integer roomId, String sessionId) {
        if (!rooms.containsKey(roomId)) return;
        rooms.get(roomId).remove(sessionId);
        log.debug("Removing session from room {} with sessionId {}", roomId, sessionId);
        if (rooms.get(roomId).isEmpty()) {
            rooms.remove(roomId);
        }
    }

    @Override
    public boolean isRoomExists(Integer roomId) {
        return rooms.containsKey(roomId);
    }

    @Override
    public List<String> getSessions(Integer roomId) {
        if (!rooms.containsKey(roomId)) return new ArrayList<>();
        return rooms.get(roomId).stream().map(BrokerSession::sessionId).toList();
    }

    @Override
    public boolean isUserInRoom(Integer roomId, String username) {
        if (!rooms.containsKey(roomId)) return false;
        return rooms.get(roomId).stream().map(BrokerSession::username).anyMatch(s -> s.equals(username));
    }

}
