package com.project.drivemodeon.exception.user.signup;

public class EmailAlreadyTaken extends BaseSignUpException {
    public EmailAlreadyTaken() {
        super("This email is already taken!", "email");
    }
}
