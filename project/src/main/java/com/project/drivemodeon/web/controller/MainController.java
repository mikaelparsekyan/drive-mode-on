package com.project.drivemodeon.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public abstract class MainController {

    //avoid creating instance
    protected MainController() {
    }

    protected ModelAndView view(String viewName) {
        return new ModelAndView(viewName);
    }

    protected ModelAndView view(String viewName, String objName, Object object) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("layouts/index");
        modelAndView.addObject("view", viewName);
        modelAndView.addObject(objName, object);

        return modelAndView;
    }
}
