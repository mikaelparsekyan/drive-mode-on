package com.project.drivemodeon.web.controller;

import com.google.gson.Gson;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.service.api.post.PostService;
import com.project.drivemodeon.service.api.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/draft")
public class DraftController {
    private final PostService postService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public DraftController(PostService postService, UserService userService, ModelMapper modelMapper, Gson gson) {
        this.postService = postService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @GetMapping
    public ModelAndView getDraftsPage(@AuthenticationPrincipal Principal principal) {
        ModelAndView modelAndView = new ModelAndView("draft");
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

    @PostMapping("/add/{id}")
    @ResponseBody
    public String addDraftAsPost(@PathVariable("id") String strId) {
        Map<String, Object> jsonResult = new HashMap<>();
        jsonResult.put("success", false);

        try {
            long id = Long.parseLong(strId);
            System.out.println(id);
        } catch (Exception e) {

        }

        return gson.toJson(jsonResult);
    }
}
