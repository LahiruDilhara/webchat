package me.lahirudilhara.webchat.dtoEntityMappers.api;

import me.lahirudilhara.webchat.dto.api.room.*;
import me.lahirudilhara.webchat.dto.api.stat.UserRoomStatResponseDTO;
import me.lahirudilhara.webchat.dto.api.stat.UserStatResponseDTO;
import me.lahirudilhara.webchat.entities.UserStatEntity;
import me.lahirudilhara.webchat.entities.room.RoomEntity;
import me.lahirudilhara.webchat.entities.room.UserRoomStatEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomEntity addMultiUserRoomDtoToRoomEntity(AddMultiUserRoomDTO addMultiUserRoomDTO);

    RoomEntity addDualUserRoomDtoToRoomEntity(AddDualUserRoomDTO addDualUserRoomDTO);

    @SubclassMapping(source = UserRoomStatEntity.class,target = UserRoomStatResponseDTO.class)
    RoomResponseDTO roomEntityToRoomResponseDTO(RoomEntity roomEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoomEntity updateMultiUserDtoToRoomEntity(UpdateMultiUserRoomDTO updateMultiUserRoomDTO);

    UserStatResponseDTO userStatEntityToUserStatResponseDTO(UserStatEntity userStatEntity);

    @Mapping(source = "memberStats", target = "userStats")
    UserRoomStatResponseDTO userRoomStatEntityToUserRoomStatResponseDTO(UserRoomStatEntity userRoomStatEntity);
}
