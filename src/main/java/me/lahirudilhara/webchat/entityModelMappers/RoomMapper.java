package me.lahirudilhara.webchat.entityModelMappers;

import me.lahirudilhara.webchat.entities.room.DualUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.room.RoomDetailsEntity;
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

    @Mapping(target = "createdBy",source = "createdBy.username")
    @SubclassMapping(target = MultiUserRoomEntity.class, source = MultiUserRoom.class)
    @SubclassMapping(target = DualUserRoomEntity.class, source = DualUserRoom.class)
    RoomEntity roomToRoomEntity(Room room);

    @Mapping(target = "createdBy",source = "multiUserRoom.createdBy.username")
    MultiUserRoomEntity multiUserRoomToMultiUserRoomEntity(MultiUserRoom  multiUserRoom, int memberCount);

    @Mapping(target = "createdBy",source = "dualUserRoom.createdBy.username")
    @Mapping(target = "user1Name",source = "username1")
    @Mapping(target = "user2Name",source = "username2")
    DualUserRoomEntity dualUserRoomToDualUserRoomEntity(DualUserRoom dualUserRoom,String username1, String username2);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdBy",ignore = true)
    void mapRoomEntityToRoom(RoomEntity roomEntity,@MappingTarget Room room);

    @Mapping(target = "createdBy",source = "roomEntity.createdBy")
    RoomDetailsEntity userRoomToUserRoomDetailsEntity(RoomEntity roomEntity, Integer unreadMessagesCount, List<UserEntity> roomMembers);
}
