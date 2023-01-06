package com.zoomania.zoomania.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class ZooManiaUserDetails implements UserDetails {
    private final Long id;
    private final String password;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final Boolean isActive;
    private final Collection<? extends GrantedAuthority> authorities;

    public ZooManiaUserDetails(Long id, String password,
                               String username,
                               String firstName,
                               String lastName,
                               Boolean isActive, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public Boolean getActive() {
        return isActive;
    }

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (getFirstName() != null) {
            fullName.append(getFullName());
        }
        if (getLastName() != null) {
            if (!fullName.isEmpty()) {
                fullName.append(" ");
            }
            fullName.append(getLastName());
        }
        return fullName.toString();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
