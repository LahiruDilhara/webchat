package me.lahirudilhara.webchat.websocket.lib.broker;

import me.lahirudilhara.webchat.websocket.lib.interfaces.RoomBroker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomBrokerImpl implements RoomBroker {
    private final Map<Integer, List<String>> rooms = new ConcurrentHashMap<>();

    @Override
    public void addSessionToRoom(Integer roomId, String sessionId) {
        if (!rooms.containsKey(roomId)){
            rooms.put(roomId,new ArrayList<>());
        }
        rooms.get(roomId).add(sessionId);
    }

    @Override
    public void removeSessionFromRoom(Integer roomId, String sessionId) {
        if(!rooms.containsKey(roomId)) return;
        rooms.get(roomId).remove(sessionId);
        if(rooms.get(roomId).isEmpty()) {
            rooms.remove(roomId);
        }
    }

    @Override
    public boolean isRoomExists(Integer roomId){
        return rooms.containsKey(roomId);
    }

    @Override
    public List<String> getSessions(Integer roomId){
        return rooms.get(roomId);
    }

}
