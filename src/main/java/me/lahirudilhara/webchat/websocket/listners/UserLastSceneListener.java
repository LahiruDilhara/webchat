package me.lahirudilhara.webchat.websocket.listners;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.service.api.user.UserService;
import me.lahirudilhara.webchat.websocket.lib.events.NewSessionEvent;
import me.lahirudilhara.webchat.websocket.lib.events.SessionDisconnectedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserLastSceneListener {

    private final UserService userService;

    public UserLastSceneListener(UserService userService) {
        this.userService = userService;
    }

    @Async
    @EventListener(NewSessionEvent.class)
    public void onUserJoined(NewSessionEvent event) {
        try{
            userService.updateUserLastScene(event.username());
        } catch (Exception e) {
            log.error("Error while updating user last scene in new user joined", e);
        }
    }

    @Async
    @EventListener(SessionDisconnectedEvent.class)
    public void onUserDisconnected(SessionDisconnectedEvent event) {
        try{
            userService.updateUserLastScene(event.username());
        }
        catch (Exception e) {
            log.error("Error while updating user last scene in new user disconnected", e);
        }
    }
}
