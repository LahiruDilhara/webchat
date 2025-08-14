package me.lahirudilhara.webchat.mappers;

import me.lahirudilhara.webchat.dto.user.UserResponseDTO;
import me.lahirudilhara.webchat.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO  userToUserResponseDTO(User user);
}
