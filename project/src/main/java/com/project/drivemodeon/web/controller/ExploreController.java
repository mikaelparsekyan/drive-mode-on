package com.project.drivemodeon.web.controller;

import com.project.drivemodeon.model.binding.comment.AddCommentBindingModel;
import com.project.drivemodeon.service.api.post.PostService;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/explore")
public class ExploreController {
    private final PostService postService;

    public ExploreController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ModelAndView getExplorePage() {
        ModelAndView modelAndView = new ModelAndView("explore");
        modelAndView.addObject("addCommentBindingModel", new AddCommentBindingModel());
        modelAndView.addObject("publicPosts",
                postService.getAllPostsByPrivacy(PostPrivacyEnum.PUBLIC));
        return modelAndView;
    }
}
