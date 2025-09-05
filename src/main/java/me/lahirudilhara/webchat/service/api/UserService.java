package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserEntity addUser(UserEntity userEntity) {
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        User user = userRepository.save(userMapper.userEntityToUser(userEntity));
        return userMapper.userToUserEntity(user);
    }

    public UserEntity userByUsername(String username){
        return userMapper.userToUserEntity(userRepository.findByUsername(username));
    }
}
