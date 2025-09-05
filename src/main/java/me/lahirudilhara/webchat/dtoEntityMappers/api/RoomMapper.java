package me.lahirudilhara.webchat.dtoEntityMappers.api;

import me.lahirudilhara.webchat.dto.api.room.AddRoomDTO;
import me.lahirudilhara.webchat.dto.api.room.RoomResponseDTO;
import me.lahirudilhara.webchat.dto.api.room.UpdateRoomDTO;
import me.lahirudilhara.webchat.entities.RoomEntity;
import me.lahirudilhara.webchat.models.Room;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomEntity addRoomDtoToRoomEntity(AddRoomDTO addRoomDTO);

    RoomResponseDTO roomEntityToRoomResponseDTO(RoomEntity roomEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoomEntity updateRoomDtoToRoomEntity(UpdateRoomDTO updateRoomDTO);
}
