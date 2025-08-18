package me.lahirudilhara.webchat.mappers.api;

import me.lahirudilhara.webchat.dto.api.user.UserResponseDTO;
import me.lahirudilhara.webchat.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO  userToUserResponseDTO(User user);
}
