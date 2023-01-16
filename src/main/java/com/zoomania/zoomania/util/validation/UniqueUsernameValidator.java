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
        if (userEntityOptional.isEmpty()) {
            return true;
        }else{
            UserEntity currentLoggedUser =
                    this.userRepository.findByUsername(authentication.getName())
                            .orElseThrow(UserNotFoundException::new);
            return currentLoggedUser.getUsername().equals(value);
        }
    }
}
