package com.zoomania.zoomania.util.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyImageValidator implements ConstraintValidator<NotEmptyImage, MultipartFile[]> {
    @Override
    public boolean isValid(MultipartFile[] value, ConstraintValidatorContext context) {
        for (int i = 0; i < value.length; i++) {
            if (value[i] == null || value[i].getOriginalFilename() == null || value[i].getOriginalFilename().isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
