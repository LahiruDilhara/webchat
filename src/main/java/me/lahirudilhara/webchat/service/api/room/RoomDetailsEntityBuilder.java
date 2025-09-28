package me.lahirudilhara.webchat.service.api.room;

import me.lahirudilhara.webchat.entities.room.RoomDetailsEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entityModelMappers.RoomMapper;
import me.lahirudilhara.webchat.models.UserRoomStatus;
import me.lahirudilhara.webchat.service.api.user.UserRoomStatusService;
import me.lahirudilhara.webchat.service.api.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomDetailsEntityBuilder {

    private final RoomQueryService roomQueryService;
    private final RoomMetricsProviderService roomMetricsProviderService;
    private final RoomMapper roomMapper;
    private final UserRoomStatusService userRoomStatusService;

    public RoomDetailsEntityBuilder(RoomQueryService roomQueryService, RoomMetricsProviderService roomMetricsProviderService, RoomMapper roomMapper, UserRoomStatusService userRoomStatusService) {
        this.roomQueryService = roomQueryService;
        this.roomMetricsProviderService = roomMetricsProviderService;
        this.roomMapper = roomMapper;
        this.userRoomStatusService = userRoomStatusService;
    }

    public RoomDetailsEntity build(RoomEntity roomEntity, UserEntity userEntity) {
        List<UserEntity> roomUsers = roomQueryService.getRoomMembers(roomEntity.getId());
        UserRoomStatus userRoomStatus = userRoomStatusService.getUserRoomStatus(userEntity.getId(), roomEntity.getId());
        int unreadMessageCountByUser = roomMetricsProviderService.roomUnreadMessageCountByUser(userRoomStatus, roomEntity.getId());
        return roomMapper.userRoomToUserRoomDetailsEntity(roomEntity,unreadMessageCountByUser,roomUsers);
    }
}
