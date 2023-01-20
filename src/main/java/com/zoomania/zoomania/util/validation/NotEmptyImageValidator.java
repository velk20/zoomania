package com.zoomania.zoomania.util.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyImageValidator implements ConstraintValidator<NotEmptyImage, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null || value.getOriginalFilename() == null || value.getOriginalFilename().isEmpty()) {
            return false;
        }

        return true;
    }
}
