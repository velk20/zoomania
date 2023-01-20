package com.zoomania.zoomania.util.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ValidImageFormatValidator implements ConstraintValidator<ValidImageFormat, MultipartFile[]> {
    private final List<String> imageContentTypesAllowed =
            new ArrayList<>()
            {{
                add("image/png");
                add("image/jpg");
                add("image/jpeg");
                add("image/svg");
                add("image/webp");
            }};
    @Override
    public boolean isValid(MultipartFile[] value, ConstraintValidatorContext context) {

        if (value.length == 1 && Objects.requireNonNull(value[0].getOriginalFilename()).isEmpty()) {
            return true;
        }
        for (int i = 0; i < value.length; i++) {
            if (!imageContentTypesAllowed.contains(value[i].getContentType())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Image format must be: " + String.join(", ", imageContentTypesAllowed))
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
