package me.lahirudilhara.webchat.service.api.user;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity addUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setLastSeen(Instant.now());
        User user = userRepository.save(userMapper.userEntityToUser(userEntity));
        return userMapper.userToUserEntity(user);
    }

    public void updateUserLastScene(String username) {
        User user = userRepository.findByUsername(username);
        user.setLastSeen(Instant.now());
        userRepository.save(user);
    }
}

//SRP says: a method (or class) should have only one reason to change.