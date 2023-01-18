package com.zoomania.zoomania.util.validation;

import com.zoomania.zoomania.exceptions.UserNotFoundException;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername,String> {
    private final UserRepository userRepository;

    public UniqueUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(value);
        if (userEntityOptional.isPresent()) {
            UserEntity currentLoggedUser =
                    this.userRepository
                            .findByUsername(authentication.getName())
                            .orElseThrow(UserNotFoundException::new);
            if (!currentLoggedUser.getUsername().equals(value)) {
                return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            }
        }
        return true;
    }
}
