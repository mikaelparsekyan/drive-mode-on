package com.project.drivemodeon.exceptions.user;

public class UserNotExistException extends Exception {
    private static final String EXCEPTION_MESSAGE = "User already exists";

    public UserNotExistException() {
        super(EXCEPTION_MESSAGE);
    }
}
