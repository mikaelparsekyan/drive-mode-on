package com.project.drivemodeon.validation;

import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.validation.annotations.UniqueNickname;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueNicknameValidator implements ConstraintValidator<UniqueNickname, String> {

    private UserService userService;

    @Override
    public void initialize(UniqueNickname constraintAnnotation) {

    }

    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext context) {
        try {
            return nickname != null && !userService.isNicknameTaken(nickname);
        } catch (NullPointerException ignored) {
            return false;
        }
    }
}
