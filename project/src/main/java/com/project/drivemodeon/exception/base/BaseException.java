package com.project.drivemodeon.exception.base;

import lombok.Getter;

@Getter
public abstract class BaseException extends Exception {
    protected String message;

    protected BaseException(String message) {
        this.message = message;
    }
}
