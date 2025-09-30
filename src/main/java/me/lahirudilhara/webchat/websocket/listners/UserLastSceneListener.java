package me.lahirudilhara.webchat.websocket.listners;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.service.api.user.UserService;
import me.lahirudilhara.webchat.websocket.dto.response.DeviceDisconnectedResponse;
import me.lahirudilhara.webchat.websocket.dto.response.NewDeviceConnectedWithRoomResponse;
import me.lahirudilhara.webchat.websocket.lib.events.NewSessionEvent;
import me.lahirudilhara.webchat.websocket.lib.events.SessionDisconnectedEvent;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import me.lahirudilhara.webchat.websocket.lib.interfaces.SessionHandler;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@Slf4j
@Service
public class UserLastSceneListener {

    private final UserService userService;
    private final SessionHandler sessionHandler;
    private final MessageBroker messageBroker;

    public UserLastSceneListener(UserService userService, SessionHandler sessionHandler, MessageBroker messageBroker) {
        this.userService = userService;
        this.sessionHandler = sessionHandler;
        this.messageBroker = messageBroker;
    }

    @Async
    @EventListener(NewSessionEvent.class)
    public void onSessionJoined(NewSessionEvent event) {
        try{
            userService.updateUserLastScene(event.username());
            List<String> otherUserSessionIds = sessionHandler.getSessionsByUser(event.username()).stream().map(WebSocketSession::getId).filter(id->!id.equals(event.sessionId())).toList();
            otherUserSessionIds.forEach(id->{
                messageBroker.sendMessageToSession(id, NewDeviceConnectedWithRoomResponse.builder().uuid(null).build());
            });
        } catch (Exception e) {
            log.error("Error while updating user last scene in new user joined", e);
        }
    }

    @Async
    @EventListener(SessionDisconnectedEvent.class)
    public void onSessionDisconnected(SessionDisconnectedEvent event) {
        try{
            userService.updateUserLastScene(event.username());
            List<String> otherUserSessionIds = sessionHandler.getSessionsByUser(event.username()).stream().map(WebSocketSession::getId).filter(id->!id.equals(event.sessionId())).toList();
            otherUserSessionIds.forEach(id->{
                messageBroker.sendMessageToSession(id, DeviceDisconnectedResponse.builder().uuid(null).build());
            });
        }
        catch (Exception e) {
            log.error("Error while updating user last scene in new user disconnected", e);
        }
    }
}
