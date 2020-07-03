package com.project.drivemodeon.exception.user;

public class InvalidUserSignUp extends Exception {
    private static final String EXCEPTION_MESSAGE = "Invalid user sign in parameters";

    public InvalidUserSignUp() {
        super(EXCEPTION_MESSAGE);
    }
}
