package com.project.drivemodeon.web.controller.advice;

import com.project.drivemodeon.exception.user.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    //    @ExceptionHandler(value = UserNotLoggedException.class)
//    public ModelAndView userNotLoggedPage() {
//        return new ModelAndView("fragments/error/user/user_not_logged");
//    }
    @ExceptionHandler(value = UserNotExistException.class)
    public ModelAndView userNotLoggedPage() {
        ModelAndView modelAndView =
                new ModelAndView("error");
        modelAndView.addObject("errCode", "404");
        modelAndView.addObject("error",
                "Ooops... User with this username do not exists!");
        return modelAndView;
    }
}
