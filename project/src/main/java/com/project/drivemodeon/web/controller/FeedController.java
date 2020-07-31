package com.project.drivemodeon.web.controller;

import com.project.drivemodeon.model.binding.comment.AddCommentBindingModel;
import com.project.drivemodeon.model.binding.post.AddPostBindingModel;
import com.project.drivemodeon.model.entity.Post;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.comment.CommentServiceModel;
import com.project.drivemodeon.model.service.post.PostServiceModel;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.model.view.PostViewModel;
import com.project.drivemodeon.service.api.comment.CommentService;
import com.project.drivemodeon.service.api.post.PostService;
import com.project.drivemodeon.service.api.user.UserService;
import com.project.drivemodeon.validation.constant.enumeration.Countries;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import com.project.drivemodeon.web.controller.advice.Advice;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Optional;

@Controller
@RequestMapping("/feed")
public class FeedController extends MainController {
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final Advice advice;
    private final ModelMapper modelMapper;

    public FeedController(PostService postService, UserService userService, CommentService commentService, Advice advice, ModelMapper modelMapper) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.advice = advice;

        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ModelAndView getMapping(@AuthenticationPrincipal Principal principal) {
        ModelAndView modelAndView = new ModelAndView("feed");
        modelAndView.addObject("addCommentBindingModel", new AddCommentBindingModel());
        modelAndView.addObject("postPrivacyEnum", PostPrivacyEnum.values());
        modelAndView.addObject("countries", new Countries().getCountries());
        if (principal != null) {
            Optional<User> loggedUser = advice.getLoggedUser(principal);

            if (loggedUser.isPresent()) {
                LinkedList<PostViewModel> allFeedPostsByUser = postService.getAllFeedPostsByUser(this.modelMapper
                        .map(loggedUser.get(), UserServiceModel.class));

                modelAndView.addObject("followingPosts", allFeedPostsByUser);
            }
        }
        modelAndView.addObject("addPostBindingModel", new AddPostBindingModel());

        return modelAndView;
    }
}
