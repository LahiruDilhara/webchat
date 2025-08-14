package me.lahirudilhara.webchat.mappers;

import me.lahirudilhara.webchat.dto.room.AddRoomDTO;
import me.lahirudilhara.webchat.dto.room.RoomResponseDTO;
import me.lahirudilhara.webchat.models.Room;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = UserMapper.class)
public interface RoomMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "isPrivate",source = "isPrivate")
    @Mapping(target = "multiUser",source = "multiUser")
    @Mapping(target = "name", source = "name")
    Room addRoomDtoToRoom(AddRoomDTO addRoomDto);

    @Mapping(source = "users",target = "members")
    RoomResponseDTO roomDtoToRoomResponseDTO(Room room);
}
