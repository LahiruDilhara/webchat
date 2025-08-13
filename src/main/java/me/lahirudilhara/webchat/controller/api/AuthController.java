package me.lahirudilhara.webchat.controller.api;

import me.lahirudilhara.webchat.jwt.JwtService;
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

    public  AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
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
