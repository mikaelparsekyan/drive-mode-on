package com.project.drivemodeon.exceptions.base;

public class BaseException extends Exception {
    protected String message;

    protected BaseException(String message) {
        this.message = message;
    }
}
