package me.lahirudilhara.webchat.dtoEntityMappers.api;

import me.lahirudilhara.webchat.dto.api.room.*;
import me.lahirudilhara.webchat.entities.room.DualUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.MultiUserRoomEntity;
import me.lahirudilhara.webchat.entities.room.RoomDetailsEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    MultiUserRoomEntity addMultiUserRoomDTOToMultiUserRoomEntity(AddMultiUserRoomDTO addMultiUserRoomDTO);

    @Mapping(target = "user2Name",source = "addingUsername")
    DualUserRoomEntity addDualUserRoomDTOToDualUserRoomEntity(AddDualUserRoomDTO addDualUserRoomDTO);

    @SubclassMapping(source = RoomDetailsEntity.class,target = RoomDetailsResponseDTO.class)
    @SubclassMapping(source = DualUserRoomEntity.class,target = DualUserRoomResponseDTO.class)
    @SubclassMapping(source = MultiUserRoomEntity.class,target = MultiUserRoomResponseDTO.class)
    RoomResponseDTO roomEntityToRoomResponseDTO(RoomEntity roomEntity,RoomDetailsEntity roomDetailsEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoomEntity updateMultiUserDtoToRoomEntity(UpdateMultiUserRoomDTO updateMultiUserRoomDTO);

    DualUserRoomResponseDTO dualUserRoomEntityToDualUserRoomResponseDTO(DualUserRoomEntity dualUserRoomEntity);

    MultiUserRoomResponseDTO multiUserRoomEntityToMultiUserRoomResponseDTO(MultiUserRoomEntity  multiUserRoomEntity);
}
