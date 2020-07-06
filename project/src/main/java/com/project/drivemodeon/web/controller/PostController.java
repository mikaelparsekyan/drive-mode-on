package com.project.drivemodeon.web.controller;

import com.project.drivemodeon.model.binding.post.AddPostBindingModel;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.post.PostServiceModel;
import com.project.drivemodeon.service.api.post.PostService;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import com.project.drivemodeon.web.controller.advice.Advice;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping("/add/post")
public class PostController {

    private final PostService postService;
    private final ModelMapper modelMapper;
    private final Advice advice;

    public PostController(PostService postService, ModelMapper modelMapper, Advice advice) {
        this.postService = postService;
        this.modelMapper = modelMapper;
        this.advice = advice;
    }

    @GetMapping
    public ModelAndView get() {
        ModelAndView modelAndView = new ModelAndView("redirect:/feed");
        modelAndView.addObject("addPostBindingModel", new AddPostBindingModel());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView post(@Valid @ModelAttribute("addPostBindingModel")
                                     AddPostBindingModel postBindingModel,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal Principal principal) {
        ModelAndView modelAndView = new ModelAndView("redirect:/feed");
        Optional<User> loggedUser = advice.getLoggedUser(principal);

        if (result.hasErrors()) {
            redirectAttributes.addAttribute("addPostBindingModel",
                    postBindingModel);

            return modelAndView;
        }
        PostServiceModel postServiceModel = modelMapper
                .map(postBindingModel, PostServiceModel.class);
        String postPrivacyValue = postBindingModel.getPostPrivacy();

        if (!Arrays.asList(PostPrivacyEnum.values()).contains(
                PostPrivacyEnum.valueOf(postPrivacyValue)) ||
                loggedUser.isEmpty()) {
            //checks if a PostPrivacyEnum is valid,
            // because user can add post via REST client!
            return new ModelAndView("redirect:/feed");
        }
        postServiceModel.setPostPrivacy(
                PostPrivacyEnum.valueOf(postPrivacyValue));
        postServiceModel.setDraft(postBindingModel.getIsDraft() == 1);
        postServiceModel.setAuthor(loggedUser.get());
        postServiceModel.setPostedOn(LocalDateTime.now());

        postService.addPost(postServiceModel);

        return modelAndView;
    }
}
