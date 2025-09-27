package me.lahirudilhara.webchat.service.api.room;

import me.lahirudilhara.webchat.entities.room.RoomDetailsEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entityModelMappers.RoomMapper;
import me.lahirudilhara.webchat.service.api.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomDetailsEntityBuilder {

    private final RoomQueryService roomQueryService;
    private final RoomMetricsProviderService roomMetricsProviderService;
    private final RoomMapper roomMapper;

    public RoomDetailsEntityBuilder(RoomQueryService roomQueryService, RoomMetricsProviderService roomMetricsProviderService, RoomMapper roomMapper) {
        this.roomQueryService = roomQueryService;
        this.roomMetricsProviderService = roomMetricsProviderService;
        this.roomMapper = roomMapper;
    }

    public RoomDetailsEntity build(RoomEntity roomEntity, UserEntity userEntity) {
        List<UserEntity> roomUsers = roomQueryService.getRoomMembers(roomEntity.getId());
        int unreadMessageCountByUser = roomMetricsProviderService.roomUnreadMessageCountByUser(userEntity, roomEntity.getId());
        return roomMapper.userRoomToUserRoomDetailsEntity(roomEntity,unreadMessageCountByUser,roomUsers);
    }
}
