package me.lahirudilhara.webchat.dtoEntityMappers.api;

import me.lahirudilhara.webchat.dto.api.room.*;
import me.lahirudilhara.webchat.dto.api.user.UserRoomStatResponseDTO;
import me.lahirudilhara.webchat.entities.RoomEntity;
import me.lahirudilhara.webchat.models.Room;
import org.mapstruct.*;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomEntity addMultiUserRoomDtoToRoomEntity(AddMultiUserRoomDTO addMultiUserRoomDTO);

    RoomEntity addDualUserRoomDtoToRoomEntity(AddDualUserRoomDTO addDualUserRoomDTO);

    RoomResponseDTO roomEntityToRoomResponseDTO(RoomEntity roomEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoomEntity updateMultiUserDtoToRoomEntity(UpdateMultiUserRoomDTO updateMultiUserRoomDTO);

    UserRoomStatResponseDTO roomEntityToUserRoomStatResponseDTO(RoomEntity roomEntity, Integer unreadMessagesCount, Instant lastAccessedAt,Integer memberCount);
}
