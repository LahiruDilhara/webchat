package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public String loginUser(UserEntity userEntity) throws BadCredentialsException {
        if(verify(userEntity)) {
            return jwtService.generateToken(userEntity.getUsername());
        }
        else{
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public UserEntity signUpUser(UserEntity userEntity) {
        return userService.addUser(userEntity);
    }

    private boolean verify(UserEntity userEntity) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEntity.getUsername(),userEntity.getPassword()));
        return authentication.isAuthenticated();
    }
}
