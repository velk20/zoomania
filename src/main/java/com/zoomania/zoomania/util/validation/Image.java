package com.zoomania.zoomania.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ImageValidator.class)
public @interface Image {
    String message() default "Invalid Image";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
