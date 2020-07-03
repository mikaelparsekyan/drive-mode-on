package com.project.drivemodeon.web.controllers;

import com.project.drivemodeon.model.binding.AddPostBindingModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/add/post")
public class PostController {

    @GetMapping
    public ModelAndView getPage() {
        ModelAndView modelAndView = new ModelAndView("layouts/index");
        modelAndView.addObject("view", "fragments/feed");
        modelAndView.addObject("addPostBindingModel", new AddPostBindingModel());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView postPage(@Valid @ModelAttribute("addPostBindingModel")
                                         AddPostBindingModel postBindingModel,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("redirect:/feed");
        if (result.hasErrors()) {
            redirectAttributes.addAttribute("addPostBindingModel",
                    postBindingModel);

            return new ModelAndView("redirect:/feed");
        }

        return modelAndView;
    }
}
