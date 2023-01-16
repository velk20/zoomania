package com.zoomania.zoomania.util.validation;


import com.zoomania.zoomania.exceptions.UserNotFoundException;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.security.Principal;
import java.util.Optional;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail,String> {
    private final UserRepository userRepository;


    public UniqueUserEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //! FIX THE EMAIL CHECKER IF THE USER IS CURRENT LOGGED USER
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(value);
        if (userEntityOptional.isEmpty()) {
            return true;
        }else{
           UserEntity currentLoggedUser =
                    this.userRepository.findByUsername(authentication.getName())
                            .orElseThrow(UserNotFoundException::new);
            return currentLoggedUser.getEmail().equals(value);
        }
    }
}
