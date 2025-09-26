package me.lahirudilhara.webchat.entityModelMappers;

import me.lahirudilhara.webchat.entities.UserRoomStatus;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.room.UserRoomStatEntity;
import me.lahirudilhara.webchat.models.Room;
import org.mapstruct.*;

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

    @Mapping(target = "createdBy",source = "room.createdBy.username")
    UserRoomStatEntity userRoomStatToUserRoomStatEntity(Room room, UserRoomStatus userRoomStatus,Integer unreadMessagesCount,Integer memberCount);
}
