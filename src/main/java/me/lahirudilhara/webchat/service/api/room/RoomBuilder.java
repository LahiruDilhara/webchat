package me.lahirudilhara.webchat.service.api.room;

import me.lahirudilhara.webchat.entities.room.DualUserRoomDetailsEntity;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomDetailsEntity;
import me.lahirudilhara.webchat.entities.room.RoomDetailsEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.models.UserRoomStatus;
import me.lahirudilhara.webchat.models.room.DualUserRoom;
import me.lahirudilhara.webchat.models.room.MultiUserRoom;
import me.lahirudilhara.webchat.models.room.Room;
import me.lahirudilhara.webchat.service.api.user.UserRoomStatusService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomBuilder {
    private final RoomQueryService roomQueryService;
    private final RoomMetricsProviderService roomMetricsProviderService;
    private final UserRoomStatusService userRoomStatusService;

    public RoomBuilder(@Lazy RoomQueryService roomQueryService, RoomMetricsProviderService roomMetricsProviderService, UserRoomStatusService userRoomStatusService) {
        this.roomQueryService = roomQueryService;
        this.roomMetricsProviderService = roomMetricsProviderService;
        this.userRoomStatusService = userRoomStatusService;
    }

    public RoomDetailsEntity buildRoomDetailsEntity(Room room,Integer userId) {
        if(room instanceof MultiUserRoom mRoom) {
            return MultiUserRoomEntityFromMultiUserRoom(mRoom,userId);
        }
        else if(room instanceof DualUserRoom dRoom) {
            return DualUserRoomEntityFromDualUserRoom(dRoom,userId);
        }
        return null;
    }

    public MultiUserRoomDetailsEntity MultiUserRoomEntityFromMultiUserRoom (MultiUserRoom room, Integer userId){
        MultiUserRoomDetailsEntity multiUserRoomDetailsEntity = new MultiUserRoomDetailsEntity();
        roomToRoomEntity(room,userId, multiUserRoomDetailsEntity);
        multiUserRoomDetailsEntity.setClosed(room.getClosed());
        multiUserRoomDetailsEntity.setIsPrivate(room.getIsPrivate());
        return multiUserRoomDetailsEntity;
    }

    public DualUserRoomDetailsEntity DualUserRoomEntityFromDualUserRoom (DualUserRoom room, Integer userId){
        return roomToRoomEntity(room,userId,new DualUserRoomDetailsEntity());
    }

    public <T extends RoomDetailsEntity> T roomToRoomEntity(Room room, Integer userId, T entity){
        List<UserEntity> roomUsers = roomQueryService.getRoomMembers(room.getId());
        UserRoomStatus userRoomStatus = userRoomStatusService.getUserRoomStatus(userId, room.getId());
        int unreadMessageCountByUser = roomMetricsProviderService.roomUnreadMessageCountByUser(userRoomStatus, room.getId());
        entity.setCreatedAt(room.getCreatedAt());
        entity.setRoomMembers(roomUsers);
        entity.setId(room.getId());
        entity.setName(room.getName());
        entity.setCreatedBy(room.getCreatedBy().getUsername());
        entity.setUnreadMessagesCount(unreadMessageCountByUser);
        return entity;
    }
}
