package me.lahirudilhara.webchat.jwt;

import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.UserRepository;
import me.lahirudilhara.webchat.service.api.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUser(username);

        if (user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException(username);
        }

        UserDetails userDetails = new SecureUserDetails(user);
        return userDetails;
    }
}
