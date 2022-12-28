package com.zoomania.zoomania.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateNowLimitValidator implements ConstraintValidator<DateNowLimit,Object> {
    @Override
    public void initialize(DateNowLimit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Integer year = (Integer) value;
        return year <= LocalDate.now().getYear();
    }
}
