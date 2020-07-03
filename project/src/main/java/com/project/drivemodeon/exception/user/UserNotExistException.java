package com.project.drivemodeon.exception.user;

public class UserNotExistException extends Exception {
    private static final String EXCEPTION_MESSAGE = "User already exists";

    public UserNotExistException() {
        super(EXCEPTION_MESSAGE);
    }
}
