package me.lahirudilhara.webchat.entityModelMappers;

import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entities.user.BaseUserEntity;
import me.lahirudilhara.webchat.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",implementationName = "entityModelUserMapper")
public interface UserMapper {
    User userEntityToUser(UserEntity userEntity);

    UserEntity userToUserEntity(User user);

    BaseUserEntity userToBaseUserEntity(User user);

    User baseUserEntityToUser(BaseUserEntity baseUserEntity);
}
