package com.project.drivemodeon.exception.user;

public class UserNotExistException extends Exception {
    private static final String EXCEPTION_MESSAGE = "User not exists";

    public UserNotExistException() {
        super(EXCEPTION_MESSAGE);
    }
}
