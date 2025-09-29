package me.lahirudilhara.webchat.entityModelMappers;

import me.lahirudilhara.webchat.entities.room.DualUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.RoomDetailsEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.models.room.DualUserRoom;
import me.lahirudilhara.webchat.models.room.MultiUserRoom;
import me.lahirudilhara.webchat.models.room.Room;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",implementationName = "entityModelRoomMapper")
public interface RoomMapper {

    @Mapping(target = "createdBy",ignore = true)
    @Mapping(target = "id",source = "roomEntity.id")
    Room roomEntityToRoom(RoomEntity roomEntity);

    @Mapping(target = "createdBy",ignore = true)
    MultiUserRoom multiUserRoomEntityToMultiUserRoom(MultiUserRoomEntity multiUserRoomEntity);

    @Mapping(target = "createdBy",ignore = true)
    DualUserRoom dualUserRoomEntityToDualUserRoom(DualUserRoomEntity dualUserRoomEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdBy",ignore = true)
    void mapMultiUserRoomEntityToMultiUserRoom(MultiUserRoomEntity roomEntity,@MappingTarget MultiUserRoom room);

    @Mapping(target = "createdBy",source = "createdBy.username")
    RoomEntity roomToRoomEntity(Room room);



}
