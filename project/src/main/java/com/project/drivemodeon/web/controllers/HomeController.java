package com.project.drivemodeon.web.controllers;

import com.project.drivemodeon.services.api.user.UserService;
import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class HomeController extends MainController {
    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/", "/index", "/home"})
    public ModelAndView getHomePage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("layouts/index");

        modelAndView.addObject("view", "fragments/home");

        return modelAndView;
    }
}
