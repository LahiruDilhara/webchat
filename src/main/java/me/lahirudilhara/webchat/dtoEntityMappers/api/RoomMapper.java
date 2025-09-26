package me.lahirudilhara.webchat.dtoEntityMappers.api;

import me.lahirudilhara.webchat.dto.api.room.*;
import me.lahirudilhara.webchat.dto.api.room.UserRoomStatResponseDTO;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.room.UserRoomStatEntity;
import org.mapstruct.*;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomEntity addMultiUserRoomDtoToRoomEntity(AddMultiUserRoomDTO addMultiUserRoomDTO);

    RoomEntity addDualUserRoomDtoToRoomEntity(AddDualUserRoomDTO addDualUserRoomDTO);

    @SubclassMapping(source = UserRoomStatEntity.class,target = UserRoomStatResponseDTO.class)
    RoomResponseDTO roomEntityToRoomResponseDTO(RoomEntity roomEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoomEntity updateMultiUserDtoToRoomEntity(UpdateMultiUserRoomDTO updateMultiUserRoomDTO);

    UserRoomStatResponseDTO userRoomStatEntityToUserRoomStatResponseDTO(UserRoomStatEntity userRoomStatEntity);
}
