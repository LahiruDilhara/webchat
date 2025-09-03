package me.lahirudilhara.webchat.dao;

import me.lahirudilhara.webchat.common.exceptions.UserNotFoundException;
import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {
    private final UserRepository userRepository;

    public UserDAO(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Cacheable(value = "userByName",key = "#username")
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Cacheable(value = "userById",key = "#id")
    public User getUserById(int id){
        return userRepository.findById(id).orElseThrow();
    }
}
