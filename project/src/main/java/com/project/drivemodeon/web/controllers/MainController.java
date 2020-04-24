package com.project.drivemodeon.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

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

    protected ModelAndView view(String viewName, Map<String, Object> objects) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("layouts/index");
        modelAndView.addObject("view", viewName);

        addObjectsToView(objects, modelAndView);

        return modelAndView;
    }

    private void addObjectsToView(Map<String, Object> objects, ModelAndView modelAndView) {
        for (var entry : objects.entrySet()) {
            modelAndView.addObject(entry.getKey(),entry.getValue());    
        }
    }
}
