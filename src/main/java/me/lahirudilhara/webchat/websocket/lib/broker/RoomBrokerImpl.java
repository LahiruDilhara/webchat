package me.lahirudilhara.webchat.websocket.lib.broker;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.lib.events.SessionLeaveRoomEvent;
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
    private final Map<Integer, List<BrokerSession>> brokerSessionsInARoom = new ConcurrentHashMap<>();
    private final Map<String, Set<Integer>> sessionIdsToRoomIds = new ConcurrentHashMap<>();
    private final Map<String,Map<Integer,Set<String>>> userConnectedRoomsAndSessions = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher applicationEventPublisher;

    public RoomBrokerImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void addSessionToRoom(Integer roomId, String sessionId, String username) {
        log.info("Adding session to room {} with sessionId {}", roomId, sessionId);
        if (brokerSessionsInARoom.containsKey(roomId)) {
            boolean alreadyExists = brokerSessionsInARoom.get(roomId)
                    .stream()
                    .anyMatch(s -> s.sessionId().equals(sessionId));
            if (alreadyExists) {
                return;
            }
        }
        if (!brokerSessionsInARoom.containsKey(roomId)) {
            brokerSessionsInARoom.put(roomId, new ArrayList<>());
        }
        brokerSessionsInARoom.get(roomId).add(new BrokerSession(username, sessionId));
        if (!sessionIdsToRoomIds.containsKey(sessionId)) {
            sessionIdsToRoomIds.put(sessionId, new HashSet<>());
        }
        sessionIdsToRoomIds.get(sessionId).add(roomId);
        if(!userConnectedRoomsAndSessions.containsKey(username)){
            userConnectedRoomsAndSessions.put(username, new ConcurrentHashMap<>());
        }
        if(!userConnectedRoomsAndSessions.get(username).containsKey(roomId)) {
            userConnectedRoomsAndSessions.get(username).put(roomId, new HashSet<>());
        }
        userConnectedRoomsAndSessions.get(username).get(roomId).add(sessionId);
        log.debug("Adding session to room {} with sessionId {}", roomId, sessionId);
        applicationEventPublisher.publishEvent(new UserJoinedRoomEvent(roomId, username));
    }

    @Override
    public void removeSessionFromRoom(Integer roomId, String sessionId) {
        log.info("Removing session from room {} with sessionId {}", roomId, sessionId);
        if (!brokerSessionsInARoom.containsKey(roomId)) return;
        BrokerSession brokerSession = brokerSessionsInARoom.get(roomId).stream().filter(bs -> bs.sessionId().equals(sessionId)).findFirst().orElse(null);
        if (brokerSession == null) return;
        String username = brokerSession.username();
        brokerSessionsInARoom.get(roomId).remove(brokerSession);
        if (brokerSessionsInARoom.get(roomId).isEmpty()) {
            brokerSessionsInARoom.remove(roomId);
        }
        if (userConnectedRoomsAndSessions.containsKey(username)) {
            if(userConnectedRoomsAndSessions.get(username).containsKey(roomId)){
                userConnectedRoomsAndSessions.get(username).get(roomId).remove(sessionId);
            }
            if(userConnectedRoomsAndSessions.get(username).get(roomId).isEmpty()){
                userConnectedRoomsAndSessions.get(username).remove(roomId);
                applicationEventPublisher.publishEvent(new UserLeaveRoomEvent(roomId,username));
            }
            if(userConnectedRoomsAndSessions.get(username).isEmpty()){
                userConnectedRoomsAndSessions.remove(username);
            }
        }
        if (sessionIdsToRoomIds.containsKey(sessionId)) {
            sessionIdsToRoomIds.get(sessionId).remove(roomId);
        }
        log.debug("Removing session from room {} with sessionId {}", roomId, sessionId);
        applicationEventPublisher.publishEvent(new SessionLeaveRoomEvent(roomId, username));
    }

    @Override
    public boolean isRoomExists(Integer roomId) {
        return brokerSessionsInARoom.containsKey(roomId);
    }

    @Override
    public List<String> getSessions(Integer roomId) {
        if (!brokerSessionsInARoom.containsKey(roomId)) return new ArrayList<>();
        return brokerSessionsInARoom.get(roomId).stream().map(BrokerSession::sessionId).toList();
    }

    @Override
    public boolean isUserInRoom(Integer roomId, String username) {
        if (!brokerSessionsInARoom.containsKey(roomId)) return false;
        return brokerSessionsInARoom.get(roomId).stream().map(BrokerSession::username).anyMatch(s -> s.equals(username));
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
        var sessions = brokerSessionsInARoom.get(roomId);
        return sessions != null && sessions.stream().anyMatch(s -> s.sessionId().equals(sessionId));
    }

    @Override
    public List<Integer> getUserOnlineRooms(String username) {
        if (!userConnectedRoomsAndSessions.containsKey(username)) return List.of();
        return userConnectedRoomsAndSessions.get(username).keySet().stream().toList();

    }

    @Override
    public List<Integer> getSessionOnlineRooms(String sessionId) {
        if (!sessionIdsToRoomIds.containsKey(sessionId)) return List.of();
        return sessionIdsToRoomIds.get(sessionId).stream().toList();
    }

    @Override
    public List<BrokerSession> getBrokerSessionsInRoom(Integer roomId) {
        if(!brokerSessionsInARoom.containsKey(roomId)) return new ArrayList<>();
        return brokerSessionsInARoom.get(roomId);
    }

    @Override
    public List<String> getRoomConnectedUsersSessions(Integer roomId, String username) {
        if(!userConnectedRoomsAndSessions.containsKey(username)) return List.of();
        if(!userConnectedRoomsAndSessions.get(username).containsKey(roomId)) return List.of();
        return userConnectedRoomsAndSessions.get(username).get(roomId).stream().toList();
    }

}
