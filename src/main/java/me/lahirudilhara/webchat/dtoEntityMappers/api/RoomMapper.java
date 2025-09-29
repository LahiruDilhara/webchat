package me.lahirudilhara.webchat.dtoEntityMappers.api;

import me.lahirudilhara.webchat.dto.api.room.*;
import me.lahirudilhara.webchat.entities.room.DualUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    MultiUserRoomEntity addMultiUserRoomDTOToMultiUserRoomEntity(AddMultiUserRoomDTO addMultiUserRoomDTO);

    DualUserRoomEntity addDualUserRoomDTOToDualUserRoomEntity(AddDualUserRoomDTO addDualUserRoomDTO);

    @SubclassMapping(source = DualUserRoomEntity.class,target = DualUserRoomDetailsResponseDTO.class)
    @SubclassMapping(source = MultiUserRoomEntity.class,target = MultiUserRoomDetailsResponseDTO.class)
    RoomDetailsResponseDTO roomEntityToRoomResponseDTO(RoomEntity roomEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MultiUserRoomEntity updateMultiUserDtoToMultiUserRoomEntity(UpdateMultiUserRoomDTO updateMultiUserRoomDTO);

    DualUserRoomDetailsResponseDTO dualUserRoomEntityToDualUserRoomResponseDTO(DualUserRoomEntity dualUserRoomEntity);

    MultiUserRoomDetailsResponseDTO multiUserRoomEntityToMultiUserRoomResponseDTO(MultiUserRoomEntity  multiUserRoomEntity);
}
