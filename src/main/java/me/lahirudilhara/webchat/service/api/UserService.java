package me.lahirudilhara.webchat.service.api;

import me.lahirudilhara.webchat.dao.UserDAO;
import me.lahirudilhara.webchat.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDAO userDAO;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserEntity addUser(UserEntity userEntity) {
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        return userDAO.saveUser(userEntity);
    }

    public boolean verify(UserEntity userEntity) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEntity.getUsername(),userEntity.getPassword()));
        return authentication.isAuthenticated();
    }

}
