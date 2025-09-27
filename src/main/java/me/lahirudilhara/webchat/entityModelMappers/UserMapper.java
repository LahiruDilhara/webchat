package me.lahirudilhara.webchat.entityModelMappers;

import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring",implementationName = "entityModelUserMapper")
public interface UserMapper {
    User userEntityToUser(UserEntity userEntity);

    UserEntity userToUserEntity(User user);
}
