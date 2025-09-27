package me.lahirudilhara.webchat.jwt;

import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.service.api.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final UserMapper userMapper;

    public JwtUserDetailsService(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getUserByUsername(username);
        System.out.println(userEntity);
        UserDetails userDetails = new SecureUserDetails(userMapper.userEntityToUser(userEntity));
        return userDetails;
    }
}
