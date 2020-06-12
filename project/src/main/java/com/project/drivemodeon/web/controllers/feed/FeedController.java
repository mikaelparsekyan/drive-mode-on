package com.project.drivemodeon.web.controllers.feed;

import com.project.drivemodeon.validation.constants.enums.PostPrivacyEnum;
import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/feed")
public class FeedController extends MainController {

    @GetMapping
    public ModelAndView getMapping() {
        ModelAndView modelAndView = new ModelAndView("layouts/index");

        modelAndView.addObject("view","fragments/feed");
        modelAndView.addObject("postPrivacyEnum", PostPrivacyEnum.values());

        return modelAndView;
    }
}
