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

    protected ModelAndView view(String viewName, String objName, Object object) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("layouts/index");
        modelAndView.addObject("view", viewName);
        modelAndView.addObject(objName, object);

        return modelAndView;
    }
}
