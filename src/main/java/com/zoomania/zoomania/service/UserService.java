package com.zoomania.zoomania.service;

import com.zoomania.zoomania.model.dto.UserRegisterDTO;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.entity.UserRoleEntity;
import com.zoomania.zoomania.model.enums.UserRoleEnum;
import com.zoomania.zoomania.repository.UserRepository;
import com.zoomania.zoomania.repository.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final ModelMapper mapper;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.mapper = mapper;
    }

    public void registerAndLogin(UserRegisterDTO userRegisterDTO) {
        UserRoleEntity byUserRoleEnum = userRoleRepository.findByUserRoleEnum(UserRoleEnum.USER);

        UserEntity userEntity = mapper.map(userRegisterDTO, UserEntity.class);
        userEntity
               .setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()))
               .addRole(byUserRoleEnum)
               .setActive(true);

        userRepository.save(userEntity);
        this.login(userEntity.getUsername());
    }

    public void login(String username) {
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(username);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.
                getContext().
                setAuthentication(auth);
    }
}
