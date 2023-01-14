package com.zoomania.zoomania.util;

import com.zoomania.zoomania.model.enums.UserRoleEnum;
import com.zoomania.zoomania.model.user.ZooManiaUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TestUserDataService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new ZooManiaUserDetails(
                1L,
                "admin",
                username,
                "Admin",
                "Adminov",
                true,
                Collections.emptyList()
        );
    }
}
