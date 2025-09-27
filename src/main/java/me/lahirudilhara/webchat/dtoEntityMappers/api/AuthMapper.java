package me.lahirudilhara.webchat.dtoEntityMappers.api;

import me.lahirudilhara.webchat.dto.api.auth.LoginDTO;
import me.lahirudilhara.webchat.dto.api.auth.SignUpDTO;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",implementationName = "dtoEntityAuthMapper")
public interface AuthMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username",source = "username")
    @Mapping(target = "password",source = "password")
    UserEntity loginDtoToUserEntity(LoginDTO loginDTO);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username",source = "username")
    @Mapping(target = "password",source = "password")
    UserEntity signUpDtoToUserEntity(SignUpDTO signUpDTO);
}
