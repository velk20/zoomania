package com.zoomania.zoomania.service;

import com.zoomania.zoomania.exceptions.ExceptionConstants;
import com.zoomania.zoomania.exceptions.UserNotFoundException;
import com.zoomania.zoomania.model.dto.user.ChangeUserPasswordDTO;
import com.zoomania.zoomania.model.dto.user.UpdateUserDTO;
import com.zoomania.zoomania.model.dto.user.UserRegisterDTO;
import com.zoomania.zoomania.model.entity.OfferEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.entity.UserRoleEntity;
import com.zoomania.zoomania.model.enums.UserRoleEnum;
import com.zoomania.zoomania.model.view.UserDetailsView;
import com.zoomania.zoomania.model.view.UserResponse;
import com.zoomania.zoomania.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final ModelMapper mapper;
    private final OfferService offerService;

    public UserService(UserRepository userRepository, UserRoleService userRoleService, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, ModelMapper mapper, OfferService offerService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.mapper = mapper;
        this.offerService = offerService;
    }


    public void registerAndLogin(UserRegisterDTO userRegisterDTO) {
        UserRoleEntity byUserRoleEnum = this.userRoleService
                .findByUserRoleEnum(UserRoleEnum.USER);

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
    public UserResponse getAllUsersAdminRest(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);

        Page<UserEntity> users = userRepository.findAll(pageable);

        // get content for page object
        List<UserEntity> listOfUsers = users.getContent();

        List<UserDetailsView> content =
                listOfUsers
                        .stream()
                        .map(this::map)
                        .collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(content);
        userResponse.setPageNo(users.getNumber());
        userResponse.setPageSize(users.getSize());
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());

        return userResponse;

    }
    public UserDetailsView getUser(String username) {
        UserEntity user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, username)));

        return this.map(user);
    }

    private UserDetailsView map(UserEntity entity) {
        UserDetailsView userDetailsView = mapper.map(entity, UserDetailsView.class);
        userDetailsView.setUserRoles(entity.getUserRoles().stream().map(r -> r.getUserRoleEnum().name()).collect(Collectors.toList()));
        userDetailsView.setActive(entity.isActive());
        userDetailsView.setAdmin(entity.getUserRoles().stream().anyMatch(r -> r.getUserRoleEnum().equals(UserRoleEnum.ADMIN)));
        return userDetailsView;
    }


    private static Pageable getPageable(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        return PageRequest.of(pageNo, pageSize, sort);
    }

    public UserDetailsView editUser(String username, UpdateUserDTO editUser, Principal principal) {
        UserEntity userEntity = this.userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, username)));

        userEntity.setPhone(editUser.getPhone())
                .setUsername(editUser.getUsername())
                .setLastName(editUser.getLastName())
                .setFirstName(editUser.getFirstName())
                .setAge(editUser.getAge())
                .setEmail(editUser.getEmail());

        UserEntity currentLoggedUser = this.userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() ->
                        new UserNotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, username)));

        if (isAdmin(currentLoggedUser)) {
            userEntity.setActive(editUser.isActive());
        }

        if (editUser.isAdmin()) {
            if (!isAdmin(userEntity)) {
                userEntity.addRole(userRoleService.findByUserRoleEnum(UserRoleEnum.ADMIN));
            }
        } else {
            if (isAdmin(userEntity)) {
                userEntity.removeRole(userRoleService.findByUserRoleEnum(UserRoleEnum.ADMIN));
            }
        }

        UserEntity editedUser = this.userRepository.save(userEntity);
        if (username.equals(principal.getName())) {
            this.login(editedUser.getUsername());
        }
        return map(editedUser);
    }

    public boolean changeUserPassword(ChangeUserPasswordDTO changeUserPasswordDTO) {
        UserEntity userEntity = this.userRepository
                .findByUsername(changeUserPasswordDTO.getUsername())
                .orElseThrow(() ->
                        new UserNotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, changeUserPasswordDTO.getUsername())));

        boolean isOldPasswordMatch =
                passwordEncoder.matches(changeUserPasswordDTO.getOldPassword(), userEntity.getPassword());

        if (!isOldPasswordMatch) {
            return false;
        } else {
            userEntity.setPassword(passwordEncoder.encode(changeUserPasswordDTO.getNewPassword()));
            this.userRepository.save(userEntity);
            return true;
        }
    }

    @Transactional
    public UserDetailsView deleteUserByUsername(String username) {
        UserEntity userEntity = this.userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, username)));

        List<OfferEntity> allOffersBySeller = this.offerService.getAllOffersBySeller(userEntity);
        for (OfferEntity offer : allOffersBySeller) {
            this.offerService.deleteOfferById(offer.getId());
        }
        this.userRepository.delete(userEntity);
        return this.map(userEntity);
    }

    public boolean isOwner(String principalName, String username) {
        if (principalName.equals(username)) {
            return true;
        }

        return userRepository.
                findByUsername(principalName).
                filter(this::isAdmin).
                isPresent();
    }

    private boolean isAdmin(UserEntity user) {
        return user.getUserRoles().
                stream().
                anyMatch(r -> r.getUserRoleEnum() == UserRoleEnum.ADMIN);
    }

    public Long getAllUsersCount() {
        return this.userRepository.count();
    }

    public Optional<UserEntity> findByEmail(String email){
        return this.userRepository.findByEmail(email);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public boolean isUserActive(String username) {
        return this.userRepository.findByUsername(username).isPresent();
    }

    public Optional<UserEntity> findById(Long id) {
        return this.userRepository.findById(id);
    }

}