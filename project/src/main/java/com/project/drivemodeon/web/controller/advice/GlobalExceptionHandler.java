package com.project.drivemodeon.web.controller.advice;

import com.project.drivemodeon.exception.user.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

//@Controller
public class GlobalExceptionHandler {

    //    @GetMapping("/error")
//    public ModelAndView handleError(HttpServletRequest httpRequest) {
//        int httpErrorCode = getErrorCode(httpRequest);
//        ModelAndView errorPage = new ModelAndView("errorPage");
//
//        String errorView = "";
//        switch (httpErrorCode) {
//            case 400: {
//                //errorView = "fragments/404";
//                break;
//            }
//            case 401: {
//                errorView = "Http Error Code: 401. Unauthorized";
//                break;
//            }
//            case 404: {
//                errorView = "fragments/404";
//                break;
//            }
//            case 500: {
//                errorView = "Http Error Code: 500. Internal Server Error";
//                break;
//            }
//        }
//        errorPage.addObject("errorView", errorView);
//        return errorPage;
//    }
//
//    private int getErrorCode(HttpServletRequest httpRequest) {
//        return (Integer) httpRequest
//                .getAttribute("javax.servlet.error.status_code");
//    }
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ModelAndView handleResourceNotFoundException() {
//        ModelAndView modelAndView = new ModelAndView("error");
//        modelAndView.addObject("errorMsg", "404 errrrrrr");
//        return modelAndView;
//    }
//
//    @ExceptionHandler(UserNotExistException.class)
//    public ModelAndView handleUserNotFoundException() {
//        ModelAndView modelAndView = new ModelAndView("error");
//        modelAndView.addObject("errorMsg", "User with this name do not exists!");
//        return modelAndView;
//    }
}
