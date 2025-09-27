package me.lahirudilhara.webchat.service.api.user;

import me.lahirudilhara.webchat.common.exceptions.UserNotFoundException;
import me.lahirudilhara.webchat.entities.user.UserEntity;
import me.lahirudilhara.webchat.entityModelMappers.UserMapper;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserQueryService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Cacheable(value = UserCacheNames.USER_BY_ID,key = "#id")
    public UserEntity getUserById(int id){
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.userToUserEntity(user);
    }

    @Cacheable(value = UserCacheNames.USER_BY_USERNAME,key = "#username")
    public UserEntity getUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        if(user == null) throw new UserNotFoundException();
        return userMapper.userToUserEntity(user);
    }
}
