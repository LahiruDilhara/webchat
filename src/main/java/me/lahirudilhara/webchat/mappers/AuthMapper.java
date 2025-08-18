package me.lahirudilhara.webchat.mappers;

import me.lahirudilhara.webchat.dto.api.auth.LoginDTO;
import me.lahirudilhara.webchat.dto.api.auth.SignUpDTO;
import me.lahirudilhara.webchat.models.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username",source = "username")
    @Mapping(target = "password",source = "password")
    User loginDtoToUser(LoginDTO loginDTO);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "username",source = "username")
    @Mapping(target = "password",source = "password")
    User signUpDtoToUser(SignUpDTO signUpDTO);
}
