package ori.config.scurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ori.entity.Roles;
import ori.entity.User;
import ori.service.IUserService;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthHelper implements UserDetailsService {

    @Autowired
    IUserService userService;
    @Autowired
    BCryptPasswordEncoder
    encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.getByUserNameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("NOT FOUND" + username));
        System.out.println("I am here [loadUserByUsername]" + user);
        return new AuthUser(user);
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuth(Set<Roles> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole().getRoleName())).collect(Collectors.toList());
    }
}
