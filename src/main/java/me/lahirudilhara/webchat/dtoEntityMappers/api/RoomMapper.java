package me.lahirudilhara.webchat.dtoEntityMappers.api;

import me.lahirudilhara.webchat.dto.api.room.*;
import me.lahirudilhara.webchat.entities.room.DualUserRoomDetailsEntity;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomDetailsEntity;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    MultiUserRoomDetailsEntity addMultiUserRoomDTOToMultiUserRoomEntity(AddMultiUserRoomDTO addMultiUserRoomDTO);

    DualUserRoomDetailsEntity addDualUserRoomDTOToDualUserRoomEntity(AddDualUserRoomDTO addDualUserRoomDTO);

    @SubclassMapping(source = DualUserRoomDetailsEntity.class,target = DualUserRoomDetailsResponseDTO.class)
    @SubclassMapping(source = MultiUserRoomDetailsEntity.class,target = MultiUserRoomDetailsResponseDTO.class)
    RoomDetailsResponseDTO roomEntityToRoomDetailsResponseDTO(RoomEntity roomEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MultiUserRoomDetailsEntity updateMultiUserDtoToMultiUserRoomEntity(UpdateMultiUserRoomDTO updateMultiUserRoomDTO);

    DualUserRoomDetailsResponseDTO dualUserRoomEntityToDualUserRoomResponseDTO(DualUserRoomDetailsEntity dualUserRoomDetailsEntity);

    MultiUserRoomDetailsResponseDTO multiUserRoomEntityToMultiUserRoomDetailsResponseDTO(MultiUserRoomDetailsEntity multiUserRoomDetailsEntity);

    @SubclassMapping(source = MultiUserRoomEntity.class,target = MultiUserRoomResponseDTO.class)
    RoomResponseDTO roomEntityToRoomResponseDTO(RoomEntity roomEntity);

    MultiUserRoomResponseDTO multiUserRoomEntityToMultiUserRoomResponseDTO(MultiUserRoomEntity multiUserRoomEntity);
}
