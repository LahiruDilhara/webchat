package me.lahirudilhara.webchat.dao;

import me.lahirudilhara.webchat.daoMappers.UserDAOMapper;
import me.lahirudilhara.webchat.entities.UserEntity;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {
    private final UserRepository userRepository;
    private final UserDAOMapper userDAOMapper;

    public UserDAO(UserRepository userRepository,UserDAOMapper userDAOMapper){
        this.userRepository = userRepository;
        this.userDAOMapper = userDAOMapper;
    }

    public UserEntity saveUser(UserEntity userEntity){
        User user = userRepository.save(userDAOMapper.userEntityToUser(userEntity));
        return userDAOMapper.userToUserEntity(user);
    }

    @Cacheable(value = "userDAOUserByName",key = "#username")
    public UserEntity getUserByUsername(String username){
        User user =  userRepository.findByUsername(username);
        return userDAOMapper.userToUserEntity(user);
    }

    @Cacheable(value = "userDAOUserById",key = "#id")
    public UserEntity getUserById(int id){
        User user = userRepository.findById(id).orElseThrow();
        return userDAOMapper.userToUserEntity(user);
    }
}
