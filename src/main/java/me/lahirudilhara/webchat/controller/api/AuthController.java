package me.lahirudilhara.webchat.controller.api;

import me.lahirudilhara.webchat.dto.user.UserResponseDTO;
import me.lahirudilhara.webchat.jwt.JwtService;
import me.lahirudilhara.webchat.mappers.UserMapper;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.service.api.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper  userMapper;

    public  AuthController(UserService userService, JwtService jwtService, UserMapper userMapper) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @PostMapping("/signup")
    public UserResponseDTO addUser(@RequestBody User user) {
        User addedUser = userService.addUser(user);
        return userMapper.userToUserResponseDTO(addedUser);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody User user) {
        if(userService.verify(user)) {
            return new ResponseEntity(jwtService.generateToken(user.getUsername()), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }
}
