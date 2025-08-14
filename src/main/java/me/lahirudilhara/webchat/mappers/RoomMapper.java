package me.lahirudilhara.webchat.mappers;

import me.lahirudilhara.webchat.dto.room.AddRoomDTO;
import me.lahirudilhara.webchat.models.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "closed", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "users",ignore = true)
    Room addRoomDtoToRoom(AddRoomDTO addRoomDto);
}
