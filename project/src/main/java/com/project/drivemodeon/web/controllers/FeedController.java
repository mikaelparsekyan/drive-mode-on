package com.project.drivemodeon.web.controllers;

import com.project.drivemodeon.model.binding.AddPostBindingModel;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/feed")
public class FeedController extends MainController {

    @GetMapping
    public ModelAndView getMapping() {
        ModelAndView modelAndView = new ModelAndView("layouts/index");
        modelAndView.addObject("view", "fragments/feed");
        modelAndView.addObject("postPrivacyEnum", PostPrivacyEnum.values());
        modelAndView.addObject("addPostBindingModel", new AddPostBindingModel());

        return modelAndView;
    }
}
