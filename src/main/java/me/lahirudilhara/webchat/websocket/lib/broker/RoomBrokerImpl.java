package me.lahirudilhara.webchat.websocket.lib.broker;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.lib.events.UserJoinedRoomEvent;
import me.lahirudilhara.webchat.websocket.lib.events.UserLeaveRoomEvent;
import me.lahirudilhara.webchat.websocket.lib.interfaces.RoomBroker;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RoomBrokerImpl implements RoomBroker {
    private final Map<Integer, List<BrokerSession>> rooms = new ConcurrentHashMap<>();
    private final Map<String, Set<Integer>> sessionIdsToRoomIds = new ConcurrentHashMap<>();
    private final Map<String, List<Integer>> userJoinedRoomIds = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher applicationEventPublisher;

    public RoomBrokerImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void addSessionToRoom(Integer roomId, String sessionId, String username) {
        if (!rooms.containsKey(roomId)) {
            rooms.put(roomId, new ArrayList<>());
        }
        rooms.get(roomId).add(new BrokerSession(username, sessionId));
        if (!sessionIdsToRoomIds.containsKey(sessionId)) {
            sessionIdsToRoomIds.put(sessionId, new HashSet<>());
        }
        sessionIdsToRoomIds.get(sessionId).add(roomId);
        if(!userJoinedRoomIds.containsKey(username)) {
            userJoinedRoomIds.put(username, new ArrayList<>());
        }
        if(!userJoinedRoomIds.get(username).contains(roomId)) {
            userJoinedRoomIds.get(username).add(roomId);
        }
        log.debug("Adding session to room {} with sessionId {}", roomId, sessionId);
        applicationEventPublisher.publishEvent(new UserJoinedRoomEvent(roomId, username));
    }

    @Override
    public void removeSessionFromRoom(Integer roomId, String sessionId) {
        if (!rooms.containsKey(roomId)) return;
        BrokerSession brokerSession = rooms.get(roomId).stream().filter(bs -> bs.sessionId().equals(sessionId)).findFirst().orElse(null);
        if (brokerSession == null) return;
        String username = brokerSession.username();
        rooms.get(roomId).remove(brokerSession);
        if (rooms.get(roomId).isEmpty()) {
            rooms.remove(roomId);
        }
        else if(userJoinedRoomIds.containsKey(username)){
            List<BrokerSession> sessions = rooms.get(roomId).stream().filter(bs->bs.username().equals(username)).toList();
            if(sessions.isEmpty()) {
                if(userJoinedRoomIds.get(username).contains(roomId)) {
                    userJoinedRoomIds.get(username).remove(roomId);
                }
                if(userJoinedRoomIds.get(username).isEmpty()){
                    userJoinedRoomIds.remove(username);
                }
            }
        }
        if (sessionIdsToRoomIds.containsKey(sessionId)) {
            sessionIdsToRoomIds.get(sessionId).remove(roomId);
        }
        log.debug("Removing session from room {} with sessionId {}", roomId, sessionId);
        applicationEventPublisher.publishEvent(new UserLeaveRoomEvent(roomId, brokerSession.username()));
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

    @Override
    public void removeFromAllRooms(String sessionId) {
        if (!sessionIdsToRoomIds.containsKey(sessionId)) return;
        Set<Integer> roomIds = sessionIdsToRoomIds.get(sessionId);
        roomIds.forEach(roomId -> {
            removeSessionFromRoom(roomId, sessionId);
        });
        sessionIdsToRoomIds.remove(sessionId);
    }

    @Override
    public boolean isSessionInRoom(Integer roomId, String sessionId) {
        if (rooms.get(roomId) == null) return false;
        if (rooms.get(roomId).isEmpty()) return false;
        if (!rooms.containsKey(roomId)) return false;
        if (rooms.get(roomId).stream().map(BrokerSession::sessionId).noneMatch(s -> s.equals(sessionId))) return false;
        return true;
    }

    @Override
    public List<Integer> getUserOnlineRooms(String username) {
        List<Integer> roomIds= userJoinedRoomIds.get(username);
        if(roomIds==null) return new ArrayList<>();
        return roomIds;
    }

    @Override
    public List<BrokerSession> getBrokerSessionsInRoom(Integer roomId) {
        if(!rooms.containsKey(roomId)) return new ArrayList<>();
        return rooms.get(roomId);
    }

}
