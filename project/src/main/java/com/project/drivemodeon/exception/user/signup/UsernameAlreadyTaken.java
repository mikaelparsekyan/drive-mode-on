package com.project.drivemodeon.exception.user.signup;

public class UsernameAlreadyTaken extends BaseSignUpException {
    public UsernameAlreadyTaken() {
        super("This username is already taken!", "username");
    }
}
