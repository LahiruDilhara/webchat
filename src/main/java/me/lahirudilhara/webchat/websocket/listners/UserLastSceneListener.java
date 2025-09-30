package me.lahirudilhara.webchat.websocket.listners;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.service.api.user.UserService;
import me.lahirudilhara.webchat.websocket.listners.events.NewUserJoinedEvent;
import me.lahirudilhara.webchat.websocket.listners.events.UserDisconnectedEvent;
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
    @EventListener(NewUserJoinedEvent.class)
    public void onUserJoined(NewUserJoinedEvent event) {
        try{
            userService.updateUserLastScene(event.username());
        } catch (Exception e) {
            log.error("Error while updating user last scene in new user joined", e);
        }
    }

    @Async
    @EventListener(UserDisconnectedEvent.class)
    public void onUserDisconnected(UserDisconnectedEvent event) {
        try{
            userService.updateUserLastScene(event.username());
        }
        catch (Exception e) {
            log.error("Error while updating user last scene in new user disconnected", e);
        }
    }
}
