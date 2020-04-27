package com.project.drivemodeon.validation;

import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.validation.annotations.UniqueEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private UserService userService;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        try {
            return email != null && !userService.isEmailTaken(email);
        } catch (NullPointerException ignored) {
            return false;
        }
    }
}
