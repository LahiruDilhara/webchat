package me.lahirudilhara.webchat.dtoEntityMappers.api;

import me.lahirudilhara.webchat.dto.api.room.*;
import me.lahirudilhara.webchat.entities.room.RoomDetailsEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomEntity addMultiUserRoomDtoToRoomEntity(AddMultiUserRoomDTO addMultiUserRoomDTO);

    RoomEntity addDualUserRoomDtoToRoomEntity(AddDualUserRoomDTO addDualUserRoomDTO);

    @SubclassMapping(source = RoomDetailsEntity.class,target = RoomDetailsResponseDTO.class)
    RoomResponseDTO roomEntityToRoomResponseDTO(RoomEntity roomEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoomEntity updateMultiUserDtoToRoomEntity(UpdateMultiUserRoomDTO updateMultiUserRoomDTO);

    RoomDetailsResponseDTO roomDetailsEntityToRoomDetailsResponseDTO(RoomDetailsEntity roomDetailsEntity);
}
