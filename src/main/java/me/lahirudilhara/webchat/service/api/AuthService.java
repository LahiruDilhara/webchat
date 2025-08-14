package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.dto.auth.LoginDTO;
import me.lahirudilhara.webchat.dto.auth.SignUpDTO;
import me.lahirudilhara.webchat.jwt.JwtService;
import me.lahirudilhara.webchat.mappers.AuthMapper;
import me.lahirudilhara.webchat.models.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private UserService userService;
    private JwtService jwtService;
    private AuthMapper authMapper;

    public AuthService(UserService userService, JwtService jwtService, AuthMapper authMapper) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authMapper = authMapper;
    }

    public String loginUser(LoginDTO loginDTO) throws BadCredentialsException {
        User user = authMapper.loginDtoToUser(loginDTO);
        if(userService.verify(user)) {
            return jwtService.generateToken(user.getUsername());
        }
        else{
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public User signUpUser(SignUpDTO signUpDTO) {
        User user = authMapper.signUpDtoToUser(signUpDTO);
        return userService.addUser(user);
    }
}
