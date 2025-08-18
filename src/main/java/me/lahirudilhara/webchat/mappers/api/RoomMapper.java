package me.lahirudilhara.webchat.mappers.api;

import me.lahirudilhara.webchat.dto.api.room.AddRoomDTO;
import me.lahirudilhara.webchat.dto.api.room.RoomResponseDTO;
import me.lahirudilhara.webchat.dto.api.room.UpdateRoomDTO;
import me.lahirudilhara.webchat.models.Room;
import org.mapstruct.*;

@Mapper(componentModel = "spring",uses = UserMapper.class)
public interface RoomMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "isPrivate",source = "isPrivate")
    @Mapping(target = "multiUser",source = "multiUser")
    @Mapping(target = "name", source = "name")
    Room addRoomDtoToRoom(AddRoomDTO addRoomDto);

    @Mapping(source = "users",target = "members")
    RoomResponseDTO roomDtoToRoomResponseDTO(Room room);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Room updateRoomDtoToRoom(UpdateRoomDTO updateRoomDTO, @MappingTarget Room room);
}
