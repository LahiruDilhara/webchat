package me.lahirudilhara.webchat.dtoEntityMappers.api;

import me.lahirudilhara.webchat.dto.api.user.UserResponseDTO;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",implementationName = "dtoEntityUserMapper")
public interface UserMapper {
    UserResponseDTO userEntityToUserResponseDTO(UserEntity userEntity);
}
