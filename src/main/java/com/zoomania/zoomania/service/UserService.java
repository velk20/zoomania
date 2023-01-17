package com.zoomania.zoomania.service;

import com.zoomania.zoomania.exceptions.UserNotFoundException;
import com.zoomania.zoomania.model.dto.user.ChangeUserPasswordDTO;
import com.zoomania.zoomania.model.dto.user.UpdateUserDTO;
import com.zoomania.zoomania.model.dto.user.UserRegisterDTO;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.entity.UserRoleEntity;
import com.zoomania.zoomania.model.enums.UserRoleEnum;
import com.zoomania.zoomania.model.view.OfferDetailsView;
import com.zoomania.zoomania.model.view.OfferResponse;
import com.zoomania.zoomania.model.view.UserDetailsView;
import com.zoomania.zoomania.model.view.UserResponse;
import com.zoomania.zoomania.repository.UserRepository;
import com.zoomania.zoomania.repository.UserRoleRepository;
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
    private final OfferService offerService;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, ModelMapper mapper, OfferService offerService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.mapper = mapper;
        this.offerService = offerService;
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

        return this.map(user);
    }

    private UserDetailsView map(UserEntity entity) {
        UserDetailsView userDetailsView = mapper.map(entity, UserDetailsView.class);
        userDetailsView.setActive(entity.isActive());
        userDetailsView.setAdmin(entity.getUserRoles().stream().anyMatch(r->r.getUserRoleEnum().equals(UserRoleEnum.ADMIN)));
        return userDetailsView;
    }

    public OfferResponse getAllOffersAdminRest(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);

        Page<OfferDetailsView> offers = offerService.getAllOffers(pageable);

        // get content for page object
        List<OfferDetailsView> listOfOffers = offers.getContent();

        return new OfferResponse()
                .setContent(listOfOffers)
                .setPageNo(offers.getNumber())
                .setPageSize(offers.getSize())
                .setTotalElements(offers.getTotalElements())
                .setTotalPages(offers.getTotalPages())
                .setLast(offers.isLast());
    }

    private static Pageable getPageable(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        return PageRequest.of(pageNo, pageSize, sort);
    }
// ! MAKE PASSWORD CHANGER FUNCTIONALITY AS PAGE !!!!!!!!!!!
    public UserDetailsView editUser(String username, UpdateUserDTO editUser) {
        UserEntity userEntity = this.userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        userEntity.setPhone(editUser.getPhone())
                .setUsername(editUser.getUsername())
                .setLastName(editUser.getLastName())
                .setFirstName(editUser.getFirstName())
                .setAge(editUser.getAge())
                .setEmail(editUser.getEmail())
                .setActive(editUser.isActive());

        if (editUser.isAdmin()) {
            if (userEntity.getUserRoles().stream()
                    .map(UserRoleEntity::getUserRoleEnum)
                    .noneMatch(r -> r.equals(UserRoleEnum.ADMIN))) {
                userEntity.addRole(userRoleRepository.findByUserRoleEnum(UserRoleEnum.ADMIN));
            }
        }else{
            if (userEntity.getUserRoles().stream()
                    .map(UserRoleEntity::getUserRoleEnum)
                    .anyMatch(r -> r.equals(UserRoleEnum.ADMIN))) {
                userEntity.removeRole(userRoleRepository.findByUserRoleEnum(UserRoleEnum.ADMIN));
            }
        }

        UserEntity editedUser = this.userRepository.save(userEntity);
        this.login(editUser.getUsername());
        return map(editedUser);
    }

    public boolean changeUserPassword(ChangeUserPasswordDTO changeUserPasswordDTO) {
        UserEntity userEntity = this.userRepository
                .findByUsername(changeUserPasswordDTO.getUsername())
                .orElseThrow(UserNotFoundException::new);

        boolean isOldPasswordMatch =
                passwordEncoder.matches(changeUserPasswordDTO.getOldPassword(),userEntity.getPassword());

        if (!isOldPasswordMatch) {
            return false;
        }else{
            userEntity.setPassword(passwordEncoder.encode(changeUserPasswordDTO.getNewPassword()));
            this.userRepository.save(userEntity);
            return true;
        }
    }
}
