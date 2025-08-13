package me.lahirudilhara.webchat.jwt;

import me.lahirudilhara.webchat.models.User;
import me.lahirudilhara.webchat.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException(username);
        }

        UserDetails userDetails = new SecureUserDetails(user);
        return userDetails;
    }
}
