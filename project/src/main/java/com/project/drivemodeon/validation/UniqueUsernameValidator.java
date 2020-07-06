package com.project.drivemodeon.validation;

import com.project.drivemodeon.service.api.user.UserService;
import com.project.drivemodeon.validation.annotation.UniqueUsername;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {

    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return false;
        }
        try {
            return !userService.isUsernameTaken(username);
        } catch (Exception e) {
            return false;
        }
    }
}
