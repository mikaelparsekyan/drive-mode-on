package com.project.drivemodeon.web.controllers.advices;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class Advice {


    @ModelAttribute("isUserLogged")
    public boolean isUserLoggedIn(HttpServletRequest request) {
        Long loggedUserId = (Long) request.getSession().getAttribute("user_id");

        return loggedUserId != null;
    }
}
