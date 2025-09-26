package me.lahirudilhara.webchat.entityModelMappers;

import me.lahirudilhara.webchat.entities.UserStatEntity;
import me.lahirudilhara.webchat.models.UserRoomStatus;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.room.UserRoomStatEntity;
import me.lahirudilhara.webchat.models.Room;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",implementationName = "entityModelRoomMapper")
public interface RoomMapper {

    @Mapping(target = "createdBy",ignore = true)
    @Mapping(target = "id",source = "roomEntity.id")
    Room roomEntityToRoom(RoomEntity roomEntity);

    @Mapping(target = "createdBy",source = "createdBy.username")
    RoomEntity roomToRoomEntity(Room room);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdBy",ignore = true)
    void mapRoomEntityToRoom(RoomEntity roomEntity,@MappingTarget Room room);

    @Mapping(target = "createdBy",source = "roomEntity.createdBy")
    @Mapping(target = "lastAccessedAt", source = "userRoomStatus.lastSeenAt")
    UserRoomStatEntity userRoomStatToUserRoomStatEntity(RoomEntity roomEntity, UserRoomStatus userRoomStatus, Integer unreadMessagesCount, Integer memberCount, List<UserStatEntity> memberStats);
}
