package com.zoomania.zoomania.service;

import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.entity.UserRoleEntity;
import com.zoomania.zoomania.model.user.ZooManiaUserDetails;
import com.zoomania.zoomania.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ZooManiaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public ZooManiaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userRepository.
                findByUsername(username).
                map(this::map).
                orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));
    }

    private UserDetails map(UserEntity userEntity) {

        return new ZooManiaUserDetails(
                userEntity.getId(),
                userEntity.getPassword(),
                userEntity.getUsername(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.isActive(),
                userEntity.getUserRoles().stream().map(this::map).toList()
        );

    }

    private GrantedAuthority map(UserRoleEntity userRole) {
        //It MUST have "ROLE_" before the real role
        return new SimpleGrantedAuthority("ROLE_" +
                userRole.
                        getUserRoleEnum().name());
    }
}
