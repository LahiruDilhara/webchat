package me.lahirudilhara.webchat.websocket.listners.updators;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.service.api.user.UserService;
import me.lahirudilhara.webchat.websocket.dto.response.DeviceDisconnectedResponse;
import me.lahirudilhara.webchat.websocket.dto.response.NewDeviceConnectedWithRoomResponse;
import me.lahirudilhara.webchat.websocket.lib.events.SessionConnectedEvent;
import me.lahirudilhara.webchat.websocket.lib.events.SessionDisconnectedEvent;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import me.lahirudilhara.webchat.websocket.lib.interfaces.SessionHandler;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserLastSceneUpdaterListener {

    private final UserService userService;

    public UserLastSceneUpdaterListener(UserService userService) {
        this.userService = userService;
    }

    @Async
    @EventListener(SessionConnectedEvent.class)
    public void onSessionJoined(SessionConnectedEvent event) {
        try{
            userService.updateUserLastScene(event.username());
        } catch (Exception e) {
            log.error("Error while updating user last scene in new user joined", e);
        }
    }

    @Async
    @EventListener(SessionDisconnectedEvent.class)
    public void onSessionDisconnected(SessionDisconnectedEvent event) {
        try{
            userService.updateUserLastScene(event.username());
        }
        catch (Exception e) {
            log.error("Error while updating user last scene in new user disconnected", e);
        }
    }
}
