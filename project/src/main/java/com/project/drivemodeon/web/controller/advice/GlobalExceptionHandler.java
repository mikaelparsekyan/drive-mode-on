package com.project.drivemodeon.web.controller.advice;

import com.project.drivemodeon.exception.user.UserNotExistException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(value = UserNotLoggedException.class)
//    public ModelAndView userNotLoggedPage() {
//        return new ModelAndView("fragments/error/user/user_not_logged");
//    }
    @ExceptionHandler(value = UserNotExistException.class)
    public ModelAndView userNotLoggedPage() {
        return new ModelAndView("fragments/error/user/user_not_exists.html");
    }
}
