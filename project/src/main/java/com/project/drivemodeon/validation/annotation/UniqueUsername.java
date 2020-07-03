package com.project.drivemodeon.validation.annotation;

import com.project.drivemodeon.validation.UniqueUsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {UniqueUsernameValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface UniqueUsername {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
