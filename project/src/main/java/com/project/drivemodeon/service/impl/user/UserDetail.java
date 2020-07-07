package com.project.drivemodeon.service.impl.user;

import com.project.drivemodeon.model.entity.AuthorityEntity;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
public class UserDetail implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetail(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional<User> userEntityOpt = userRepository.findUserByUsername(username);
        return userEntityOpt.
                map(this::map).
                orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));
    }

    private UserDetails map(User userEntity) {
        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.
                        getAuthorities().
                        stream().
                        map(this::map).
                        collect(Collectors.toList())
        );
    }

    private GrantedAuthority map(AuthorityEntity authorityEntity) {
        return new SimpleGrantedAuthority(authorityEntity.getName());
    }
}
