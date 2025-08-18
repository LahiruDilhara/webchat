package me.lahirudilhara.webchat.controller.api;

import jakarta.validation.Valid;
import me.lahirudilhara.webchat.dto.api.auth.LoginDTO;
import me.lahirudilhara.webchat.dto.api.auth.SignUpDTO;
import me.lahirudilhara.webchat.dto.api.jwt.JwtResponseDTO;
import me.lahirudilhara.webchat.dto.api.user.UserResponseDTO;
import me.lahirudilhara.webchat.mappers.UserMapper;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.service.api.AuthService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserMapper  userMapper;
    private final AuthService authService;

    public  AuthController( UserMapper userMapper, AuthService authService) {
        this.userMapper = userMapper;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public UserResponseDTO addUser(@Valid @RequestBody SignUpDTO signUpDTO) {
        User user = authService.signUpUser(signUpDTO);
        return userMapper.userToUserResponseDTO(user);
    }

    @PostMapping("/login")
    public JwtResponseDTO login(@Valid @RequestBody LoginDTO loginDTO) throws BadCredentialsException {
        String token = authService.loginUser(loginDTO);
        return new JwtResponseDTO(token);
    }
}
