package com.project.drivemodeon.validation;

import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.validation.annotations.UniqueNickname;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueNicknameValidator implements ConstraintValidator<UniqueNickname, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UniqueNickname constraintAnnotation) {

    }

    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext context) {
        if(nickname == null){
            return false;
        }

        return !userService.isNicknameTaken(nickname);
    }
}
