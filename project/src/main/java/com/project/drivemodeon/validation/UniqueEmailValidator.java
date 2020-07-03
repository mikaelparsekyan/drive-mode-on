package com.project.drivemodeon.validation;

import com.project.drivemodeon.services.api.user.UserService;
import com.project.drivemodeon.validation.annotation.UniqueEmail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false;
        }
        try {
            return !userService.isEmailTaken(email);
        } catch (Exception e) {
            return false;
        }
    }
}
