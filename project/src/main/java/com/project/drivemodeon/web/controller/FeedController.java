package com.project.drivemodeon.web.controller;

import com.project.drivemodeon.model.binding.post.AddPostBindingModel;
import com.project.drivemodeon.validation.constant.enumeration.Countries;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/feed")
public class FeedController extends MainController {

    @GetMapping
    public ModelAndView getMapping() {
        ModelAndView modelAndView = new ModelAndView("feed");
        modelAndView.addObject("postPrivacyEnum", PostPrivacyEnum.values());
        modelAndView.addObject("countries", new Countries().getCountries());
        modelAndView.addObject("addPostBindingModel", new AddPostBindingModel());

        return modelAndView;
    }
}
