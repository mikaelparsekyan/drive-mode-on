package com.project.drivemodeon.web.controller;

import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.service.api.post.PostService;
import com.project.drivemodeon.service.api.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@RequestMapping("/drafts")
public class DraftController {
    private final PostService postService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public DraftController(PostService postService, UserService userService, ModelMapper modelMapper) {
        this.postService = postService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ModelAndView getDraftsPage(@AuthenticationPrincipal Principal principal) {
        ModelAndView modelAndView = new ModelAndView("drafts");
        if (principal == null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        User userByUsername = userService.getUserByUsername(principal.getName());
        if (userByUsername == null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }

        modelAndView.addObject("userDrafts",
                postService.getAllDraftsByUser(
                        modelMapper.map(userByUsername, UserServiceModel.class)
                )
        );
        return modelAndView;
    }
}
