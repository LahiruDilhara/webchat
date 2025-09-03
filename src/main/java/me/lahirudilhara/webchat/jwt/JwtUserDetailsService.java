package me.lahirudilhara.webchat.jwt;

import me.lahirudilhara.webchat.dao.UserDAO;
import me.lahirudilhara.webchat.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserDAO userDAO;

    public JwtUserDetailsService( UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.getUserByUsername(username);

        if (user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException(username);
        }

        UserDetails userDetails = new SecureUserDetails(user);
        return userDetails;
    }
}
