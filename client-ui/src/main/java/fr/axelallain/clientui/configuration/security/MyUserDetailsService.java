package fr.axelallain.clientui.configuration.security;

import fr.axelallain.clientui.model.User;
import fr.axelallain.clientui.proxy.UsersProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersProxy usersProxy;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = usersProxy.usersByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Email not found");
        }

        return new UserPrincipal(user);
    }
}
