package com.project.drivemodeon.exception.user.signup;

public class PasswordsNotMatch extends BaseSignUpException {
    public PasswordsNotMatch() {
        super("Passwords did not match!","password");
    }
}
