package com.project.drivemodeon.exceptions.user;

public class InvalidUserSignUp extends Exception {
    private static final String EXCEPTION_MESSAGE = "Invalid user sign in parameters";

    public InvalidUserSignUp() {
        super(EXCEPTION_MESSAGE);
    }
}
