package com.project.drivemodeon.web.controller;

import com.google.gson.Gson;
import com.project.drivemodeon.exception.user.UserNotExistException;
import com.project.drivemodeon.exception.user.UserNotLoggedException;
import com.project.drivemodeon.model.binding.post.AddPostBindingModel;
import com.project.drivemodeon.model.entity.BaseEntity;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.post.PostServiceModel;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.model.view.UserViewModel;
import com.project.drivemodeon.service.api.post.PostService;
import com.project.drivemodeon.service.api.user.UserService;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import com.project.drivemodeon.web.controller.advice.Advice;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Advice advice;
    private final Gson gson;

    public PostController(PostService postService, UserService userService, ModelMapper modelMapper, Advice advice, Gson gson) {
        this.postService = postService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.advice = advice;
        this.gson = gson;
    }

    @GetMapping("/add")
    public ModelAndView getAllPostPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("redirect:/feed");

        if (!model.containsAttribute("addPostBindingModel")) {
            modelAndView.addObject("addPostBindingModel", new AddPostBindingModel());
        }
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addPost(@Valid @ModelAttribute("addPostBindingModel")
                                        AddPostBindingModel addPostBindingModel,
                                BindingResult result,
                                @AuthenticationPrincipal Principal principal) {
        ModelAndView modelAndView = new ModelAndView("redirect:/feed");
        Optional<User> loggedUser = advice.getLoggedUser(principal);

        if (result.hasErrors()) {
            modelAndView.addObject("error", true);
            return modelAndView;
        }
        PostServiceModel postServiceModel = modelMapper
                .map(addPostBindingModel, PostServiceModel.class);
        String postPrivacyValue = addPostBindingModel.getPostPrivacy();

        if (!Arrays.asList(PostPrivacyEnum.values()).contains(
                PostPrivacyEnum.valueOf(postPrivacyValue)) ||
                loggedUser.isEmpty()) {
            //checks if a PostPrivacyEnum is valid,
            // because user can add post via REST client!
            return new ModelAndView("redirect:/feed");
        }
        postServiceModel.setPostPrivacy(
                PostPrivacyEnum.valueOf(postPrivacyValue));
        postServiceModel.setDraft(addPostBindingModel.getIsDraft() == 1);
        postServiceModel.setAuthor(loggedUser.get());
        postServiceModel.setPostedOn(LocalDateTime.now());

        postService.addPost(postServiceModel);

        return modelAndView;
    }

    @PostMapping("/like/{id}")
    @ResponseBody
    public String likePost(@PathVariable("id") String id,
                           @AuthenticationPrincipal Principal principal) {
        Map<String, Object> jsonResult = new HashMap<>();
        long postId;
        try {
            postId = Long.parseLong(id);
        } catch (Exception e) {
            jsonResult.put("success", false);
            return gson.toJson(jsonResult, HashMap.class);
        }
        if (principal == null) {
            jsonResult.put("success", false);
            return gson.toJson(jsonResult, HashMap.class);
        }
        Optional<PostServiceModel> postById = postService.getPostById(postId);
        User user = userService.getUserByUsername(principal.getName());

        if (postById.isEmpty() || postById.get().getLikers().contains(user)) {
            jsonResult.put("success", false);
            return gson.toJson(jsonResult, HashMap.class);
        }

        postService.likePost(postId, user.getUsername());

        jsonResult.put("success", true);
        return gson.toJson(jsonResult, HashMap.class);
    }

    @PostMapping("/dislike/{id}")
    @ResponseBody
    public String dislikePost(@PathVariable("id") String id,
                              @AuthenticationPrincipal Principal principal) {
        Map<String, Object> jsonResult = new HashMap<>();
        long postId;
        try {
            postId = Long.parseLong(id);
        } catch (Exception e) {
            jsonResult.put("success", false);
            return gson.toJson(jsonResult, HashMap.class);
        }
        if (principal == null) {
            jsonResult.put("success", false);
            return gson.toJson(jsonResult, HashMap.class);
        }
        Optional<PostServiceModel> postById = postService.getPostById(postId);
        User user = userService.getUserByUsername(principal.getName());

        if (postById.isEmpty() || !postById.get().getLikers().contains(user)) {
            jsonResult.put("success", false);
            return gson.toJson(jsonResult, HashMap.class);
        }

        postService.dislikePost(postId, user.getUsername());

        jsonResult.put("success", true);
        return gson.toJson(jsonResult, HashMap.class);
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteDraft(@PathVariable("id") String strId,
                                    @AuthenticationPrincipal Principal principal) throws UserNotLoggedException, UserNotExistException {
        ModelAndView modelAndView = new ModelAndView("redirect:/feed");

        if (principal == null) {
            throw new UserNotLoggedException();
        }
        long id = 0;
        try {
            id = Long.parseLong(strId);
            System.out.println(id);
        } catch (Exception e) {
            return modelAndView;
        }

        Optional<PostServiceModel> postById = postService.getPostById(id);
        User user = userService.getUserByUsername(principal.getName());

        if (user == null) {
            throw new UserNotExistException();
        }

        if (postById.isEmpty()) {
            return modelAndView;
        }
        LinkedList<String> authorities = user.getAuthorities()
                .stream().map(authorityEntity -> authorityEntity.getName())
                .collect(Collectors.toCollection(LinkedList::new));
        if ((postById.get().getAuthor().getId() == user.getId()) || authorities.contains("ROLE_ADMIN")) {
            postService.deletePost(postById.get());
        }

        return modelAndView;
    }
}
