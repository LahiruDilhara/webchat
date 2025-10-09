package me.lahirudilhara.webchat.websocket.listners;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.service.api.user.UserRoomStatusService;
import me.lahirudilhara.webchat.websocket.lib.events.SessionLeaveRoomEvent;
import me.lahirudilhara.webchat.websocket.lib.events.UserJoinedRoomEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRoomLastSceneListener {

    private final UserRoomStatusService userRoomStatusService;

    public UserRoomLastSceneListener(UserRoomStatusService userRoomStatusService) {
        this.userRoomStatusService = userRoomStatusService;
    }

    @Async
    @EventListener(UserJoinedRoomEvent.class)
    public void onUserJoinedRoomEvent(UserJoinedRoomEvent event) {
        try{
            userRoomStatusService.updateUserRoomLastScene(event.username(),event.roomId());
        }
        catch (Exception e){
            log.error("Error while updating userRoom last scene in new room join. Username is {}",event.username(), e);
        }
    }

    @Async
    @EventListener(SessionLeaveRoomEvent.class)
    public void onUserLeaveRoomEvent(SessionLeaveRoomEvent event) {
        try{
            userRoomStatusService.updateUserRoomLastScene(event.username(),event.roomId());
        }
        catch (Exception e){
            log.error("Error while updating userRoom last scene in leave room.Username is {}",event.username(), e);
        }
    }

}
