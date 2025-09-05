package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.dto.api.auth.LoginDTO;
import me.lahirudilhara.webchat.dto.api.auth.SignUpDTO;
import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.jwt.JwtService;
import me.lahirudilhara.webchat.mappers.api.AuthMapper;
import me.lahirudilhara.webchat.models.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UserService userService;
    private JwtService jwtService;

    public AuthService(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public String loginUser(UserEntity userEntity) throws BadCredentialsException {
        if(userService.verify(userEntity)) {
            return jwtService.generateToken(userEntity.getUsername());
        }
        else{
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public UserEntity signUpUser(UserEntity userEntity) {
        return userService.addUser(userEntity);
    }
}
