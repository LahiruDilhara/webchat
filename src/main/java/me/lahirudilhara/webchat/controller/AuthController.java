package me.lahirudilhara.webchat.controller;

import jakarta.validation.Valid;
import me.lahirudilhara.webchat.dto.api.auth.LoginDTO;
import me.lahirudilhara.webchat.dto.api.auth.SignUpDTO;
import me.lahirudilhara.webchat.dto.api.jwt.JwtResponseDTO;
import me.lahirudilhara.webchat.dto.api.user.UserResponseDTO;
import me.lahirudilhara.webchat.dtoEntityMappers.api.AuthMapper;
import me.lahirudilhara.webchat.dtoEntityMappers.api.UserMapper;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.service.api.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserMapper  userMapper;
    private final AuthMapper authMapper;
    private final AuthService authService;

    public  AuthController( UserMapper userMapper, AuthService authService,AuthMapper authMapper) {
        this.userMapper = userMapper;
        this.authService = authService;
        this.authMapper = authMapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        UserEntity userEntity = authService.signUpUser(authMapper.signUpDtoToUserEntity(signUpDTO));
        return new ResponseEntity<>(userMapper.userEntityToUserResponseDTO(userEntity),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) throws BadCredentialsException {
        String token = authService.loginUser(authMapper.loginDtoToUserEntity(loginDTO));
        return new ResponseEntity<>(new JwtResponseDTO(token),HttpStatus.CREATED);
    }
}
