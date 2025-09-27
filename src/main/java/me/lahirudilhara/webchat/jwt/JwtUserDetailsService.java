package me.lahirudilhara.webchat.jwt;

import me.lahirudilhara.webchat.entities.user.BaseUserEntity;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.service.api.user.UserQueryService;
import me.lahirudilhara.webchat.service.api.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;
    private final UserQueryService userQueryService;

    public JwtUserDetailsService(UserMapper userMapper, UserQueryService userQueryService) {
        this.userMapper = userMapper;
        this.userQueryService = userQueryService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userQueryService.getUserByUsername(username);
        UserDetails userDetails = new SecureUserDetails(userMapper.userEntityToUser(userEntity));
        return userDetails;
    }
}
