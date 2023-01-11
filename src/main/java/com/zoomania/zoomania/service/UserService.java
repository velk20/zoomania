package com.zoomania.zoomania.service;

import com.zoomania.zoomania.model.dto.UserRegisterDTO;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.entity.UserRoleEntity;
import com.zoomania.zoomania.model.enums.UserRoleEnum;
import com.zoomania.zoomania.model.view.UserDetailsView;
import com.zoomania.zoomania.repository.UserRepository;
import com.zoomania.zoomania.repository.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<UserDetailsView> getAllUsers() {

        return this.userRepository.findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
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

    public UserDetailsView getUser(String username) {
        UserEntity user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " was not found!"));

        return mapper.map(user,UserDetailsView.class);
    }

    private UserDetailsView map(UserEntity entity) {
        UserDetailsView userDetailsView = mapper.map(entity, UserDetailsView.class);
        userDetailsView.setUserRoles(entity.getUserRoles().stream().map(u -> u.getUserRoleEnum().name()).collect(Collectors.toList()));
        return userDetailsView;
    }

}
