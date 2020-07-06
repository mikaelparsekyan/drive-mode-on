package com.project.drivemodeon.exception.user;

import com.project.drivemodeon.exception.user.signup.BaseSignUpException;

public class InvalidUserSignUp extends BaseSignUpException {
    private static final String EXCEPTION_MESSAGE = "Something went wrong!";

    public InvalidUserSignUp() {
        super(EXCEPTION_MESSAGE,"");
    }
}
