package com.project.drivemodeon.web.controllers.users;

import com.project.drivemodeon.web.controllers.advices.Advice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/edit/profile")
public class UserEditController {
    private final Advice adviceController;

    public UserEditController(Advice adviceController) {
        this.adviceController = adviceController;
    }

    @GetMapping
    public ModelAndView getUserSettingsPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("layouts/index");
        modelAndView.addObject("view", "fragments/user/edit_user");
        return modelAndView;
    }
}
