package com.project.drivemodeon.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    //avoid creating instance
    protected MainController() {
    }

    protected ModelAndView view(String viewName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("layouts/index");
        modelAndView.addObject("view", viewName);
        return modelAndView;
    }
}
