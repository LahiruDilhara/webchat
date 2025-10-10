package me.lahirudilhara.webchat.websocket.lib.broker;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.lib.events.SessionDisconnectedEvent;
import me.lahirudilhara.webchat.websocket.lib.events.SessionLeaveRoomEvent;
import me.lahirudilhara.webchat.websocket.lib.events.UserJoinedRoomEvent;
import me.lahirudilhara.webchat.websocket.lib.events.UserLeaveRoomEvent;
import me.lahirudilhara.webchat.websocket.lib.interfaces.RoomBroker;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class RoomBrokerImpl implements RoomBroker {
    private final Map<Integer, List<BrokerSession>> roomIdToBrokerSessions = new ConcurrentHashMap<>();
    private final Map<String, Set<Integer>> sessionIdToRoomIds = new ConcurrentHashMap<>();
    private final Map<String,Map<Integer,Set<String>>> userConnectedRoomsAndSessions = new ConcurrentHashMap<>();

    private final ApplicationEventPublisher applicationEventPublisher;

    public RoomBrokerImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void addSessionToRoom(Integer roomId, String sessionId, String username) {
        log.debug("Adding session to room {} with sessionId {}", roomId, sessionId);

        if (roomIdToBrokerSessions.containsKey(roomId)) {
            boolean alreadyExists = roomIdToBrokerSessions.get(roomId)
                    .stream()
                    .anyMatch(s -> s.sessionId().equals(sessionId));
            if (alreadyExists) {
                log.debug("Session {} already exists in room {}", sessionId,roomId);
                return;
            }
        }

        // Add session to room broker map
        if (!roomIdToBrokerSessions.containsKey(roomId)) {
            roomIdToBrokerSessions.put(roomId, new ArrayList<>());
        }
        roomIdToBrokerSessions.get(roomId).add(new BrokerSession(username, sessionId));

        // Add connected room to the session
        if (!sessionIdToRoomIds.containsKey(sessionId)) {
            sessionIdToRoomIds.put(sessionId, new HashSet<>());
        }
        sessionIdToRoomIds.get(sessionId).add(roomId);

        // Add connected room and connected session to user
        if(!userConnectedRoomsAndSessions.containsKey(username)){
            userConnectedRoomsAndSessions.put(username, new ConcurrentHashMap<>());
        }
        if(!userConnectedRoomsAndSessions.get(username).containsKey(roomId)) {
            userConnectedRoomsAndSessions.get(username).put(roomId, new HashSet<>());
        }
        userConnectedRoomsAndSessions.get(username).get(roomId).add(sessionId);

        log.debug("Added the session to room {} with sessionId {}", roomId, sessionId);
        applicationEventPublisher.publishEvent(new UserJoinedRoomEvent(roomId, username));
    }

    private String removeSessionFromUserConnectedRoomsAndSessions(Integer roomId, String sessionId){
        BrokerSession brokerSession = roomIdToBrokerSessions.get(roomId).stream().filter(bs -> bs.sessionId().equals(sessionId)).findFirst().orElse(null);
        if(brokerSession == null) return null;
        String username =  brokerSession.username();
        if(!userConnectedRoomsAndSessions.containsKey(username)) return null;
        if(!userConnectedRoomsAndSessions.get(username).containsKey(roomId)) return null;
        if(!userConnectedRoomsAndSessions.get(username).get(roomId).contains(sessionId)) return null;
        userConnectedRoomsAndSessions.get(username).get(roomId).remove(sessionId);
        if(userConnectedRoomsAndSessions.get(username).get(roomId).isEmpty()){
            userConnectedRoomsAndSessions.get(username).remove(roomId);
            applicationEventPublisher.publishEvent(new UserLeaveRoomEvent(roomId, username));
        }
        if(userConnectedRoomsAndSessions.get(username).isEmpty()){
            userConnectedRoomsAndSessions.remove(username);
        }
        return username;
    }

    private void removeSessionFromSessionIdToRoomIds(Integer roomId, String sessionId){
        if(!sessionIdToRoomIds.containsKey(sessionId)) return;
        sessionIdToRoomIds.remove(sessionId);
    }

    private void removeSessionFromRoomIdToBrokerSessions(Integer roomId, String sessionId){
        if(!roomIdToBrokerSessions.containsKey(roomId)) return;
        if(roomIdToBrokerSessions.get(roomId).stream().noneMatch(bs->bs.sessionId().equals(sessionId))) return;
        roomIdToBrokerSessions.get(roomId).removeIf(bs->bs.sessionId().equals(sessionId));
        if(roomIdToBrokerSessions.get(roomId).isEmpty()){
            roomIdToBrokerSessions.remove(roomId);
        }
    }

    @Override
    public void removeSessionFromRoom(Integer roomId, String sessionId) {
        log.debug("Removing session from room {} with sessionId {}", roomId, sessionId);
        String username = removeSessionFromUserConnectedRoomsAndSessions(roomId, sessionId);
        removeSessionFromSessionIdToRoomIds(roomId, sessionId);
        removeSessionFromRoomIdToBrokerSessions(roomId, sessionId);
        log.debug("Removed the session from room {} with sessionId {}", roomId, sessionId);
        if(username == null){
            log.debug("When removing session {} From room {}. The username become null",sessionId,roomId);
            return;
        }
        applicationEventPublisher.publishEvent(new SessionLeaveRoomEvent(roomId, username));
    }

    @Override
    public boolean isRoomExists(Integer roomId) {
        return roomIdToBrokerSessions.containsKey(roomId);
    }

    @Override
    public List<String> getSessions(Integer roomId) {
        if (!roomIdToBrokerSessions.containsKey(roomId)) return new ArrayList<>();
        return roomIdToBrokerSessions.get(roomId).stream().map(BrokerSession::sessionId).toList();
    }

    @Override
    public boolean isUserInRoom(Integer roomId, String username) {
        if (!roomIdToBrokerSessions.containsKey(roomId)) return false;
        return roomIdToBrokerSessions.get(roomId).stream().map(BrokerSession::username).anyMatch(s -> s.equals(username));
    }

    @Override
    public boolean isSessionInRoom(Integer roomId, String sessionId) {
        var sessions = roomIdToBrokerSessions.get(roomId);
        return sessions != null && sessions.stream().anyMatch(s -> s.sessionId().equals(sessionId));
    }

    @Override
    public List<Integer> getUserOnlineRooms(String username) {
        if (!userConnectedRoomsAndSessions.containsKey(username)) return List.of();
        return userConnectedRoomsAndSessions.get(username).keySet().stream().toList();

    }

    @Override
    public List<Integer> getSessionOnlineRooms(String sessionId) {
        if (!sessionIdToRoomIds.containsKey(sessionId)) return List.of();
        return sessionIdToRoomIds.get(sessionId).stream().toList();
    }

    @Override
    public List<BrokerSession> getBrokerSessionsInRoom(Integer roomId) {
        if(!roomIdToBrokerSessions.containsKey(roomId)) return new ArrayList<>();
        return roomIdToBrokerSessions.get(roomId);
    }

    @Override
    public List<String> getRoomConnectedUsersSessions(Integer roomId, String username) {
        if(!userConnectedRoomsAndSessions.containsKey(username)) return List.of();
        if(!userConnectedRoomsAndSessions.get(username).containsKey(roomId)) return List.of();
        return userConnectedRoomsAndSessions.get(username).get(roomId).stream().toList();
    }

    @EventListener(SessionDisconnectedEvent.class)
    public void onSessionDisconnectedEvent(SessionDisconnectedEvent event){
        Set<Integer> sessionConnectedRoomIds = sessionIdToRoomIds.get(event.sessionId());
        if(sessionConnectedRoomIds == null) return;
        sessionConnectedRoomIds.forEach(roomId -> {
            removeSessionFromRoomIdToBrokerSessions(roomId, event.sessionId());
        });
    }
}
