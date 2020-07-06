package com.project.drivemodeon.exception.user.signup;

import com.project.drivemodeon.exception.base.BaseException;
import lombok.Getter;

@Getter
public class BaseSignUpException extends BaseException {
    private String fieldName;

    protected BaseSignUpException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }
}
