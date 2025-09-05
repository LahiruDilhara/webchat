package me.lahirudilhara.webchat.daoMappers;

import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDAOMapper {
    User userEntityToUser(UserEntity userEntity);

    UserEntity userToUserEntity(User user);
}
