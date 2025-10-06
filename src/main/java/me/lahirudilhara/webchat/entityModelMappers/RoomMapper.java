package me.lahirudilhara.webchat.entityModelMappers;

import me.lahirudilhara.webchat.entities.room.DualUserRoomDetailsEntity;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomDetailsEntity;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.models.room.DualUserRoom;
import me.lahirudilhara.webchat.models.room.MultiUserRoom;
import me.lahirudilhara.webchat.models.room.Room;
import org.mapstruct.*;

@Mapper(componentModel = "spring",implementationName = "entityModelRoomMapper")
public interface RoomMapper {

    @Mapping(target = "createdBy",ignore = true)
    @Mapping(target = "id",source = "roomEntity.id")
    Room roomEntityToRoom(RoomEntity roomEntity);

    @Mapping(target = "createdBy",ignore = true)
    MultiUserRoom multiUserRoomEntityToMultiUserRoom(MultiUserRoomDetailsEntity multiUserRoomDetailsEntity);

    @Mapping(target = "createdBy",ignore = true)
    DualUserRoom dualUserRoomEntityToDualUserRoom(DualUserRoomDetailsEntity dualUserRoomDetailsEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdBy",ignore = true)
    void mapMultiUserRoomEntityToMultiUserRoom(MultiUserRoomDetailsEntity roomEntity, @MappingTarget MultiUserRoom room);

    @Mapping(target = "createdBy",source = "createdBy.username")
    RoomEntity roomToRoomEntity(Room room);

    @Mapping(target = "createdBy",source = "multiUserRoom.createdBy.username")
    @Mapping(target = "memberCount", source = "memberCount")
    MultiUserRoomEntity multiUserRoomToMultiUserRoomEntity(MultiUserRoom multiUserRoom,Integer memberCount);
}
