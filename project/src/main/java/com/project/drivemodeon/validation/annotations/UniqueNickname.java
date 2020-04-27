package com.project.drivemodeon.validation.annotations;

import com.project.drivemodeon.validation.UniqueNicknameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {UniqueNicknameValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface UniqueNickname {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
