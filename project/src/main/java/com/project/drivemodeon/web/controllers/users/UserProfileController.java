package com.project.drivemodeon.web.controllers.users;

import com.google.gson.Gson;
import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.web.controllers.MainController;
import com.project.drivemodeon.web.controllers.advices.Advice;
import com.project.drivemodeon.web.view_models.user.UserProfileViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserProfileController extends MainController {

    private final UserService userService;

    private final Gson gson;

    private final Advice advice;

    private final ModelMapper modelMapper;

    public UserProfileController(UserService userService, Gson gson, Advice advice, ModelMapper modelMapper) {
        this.userService = userService;
        this.gson = gson;
        this.advice = advice;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{username}")
    public ModelAndView getUserProfile(@PathVariable String username,
                                       HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("layouts/index");

        Optional<UserProfileViewModel> loggedUser = advice.getLoggedUser(request);
        Optional<User> currentPageUser = userService.getUserByUsername(username);

        if (currentPageUser.isPresent()) {
            //TODO map other fields (bio, f_name, l_name ...)
            UserProfileViewModel currentUserViewModel = modelMapper
                    .map(currentPageUser.get(), UserProfileViewModel.class);

            modelAndView.addObject("view", "fragments/user/user_profile");
            modelAndView.addObject("userViewModel", currentUserViewModel);

            if (loggedUser.isPresent()) {
                User sessionUser = modelMapper.map(loggedUser.get(), User.class);

                modelAndView.addObject("isUserFollowCurrentProfile",
                        userService.isCurrentUserFollowProfileUser(
                                sessionUser, currentPageUser.get()));
            }
            return modelAndView;
        }
        modelAndView.setViewName("fragments/errors/user/user_not_found");
        return modelAndView;
    }

    @PostMapping("/follow/{username}")
    @ResponseBody
    public String followUser(@PathVariable String username, HttpServletRequest request) {
        Map<String, Object> jsonResult = new HashMap<>();

        Optional<UserProfileViewModel> loggedUser = advice.getLoggedUser(request);
        Optional<User> followingUser = userService.getUserByUsername(username);

        if (loggedUser.isPresent() && followingUser.isPresent()) {
            User userProfileViewModel = modelMapper
                    .map(loggedUser.get(), User.class);

            userService.followUser(userProfileViewModel, followingUser.get());

            jsonResult.put("success", true);
        } else {
            jsonResult.put("success", false);
        }

        return gson.toJson(jsonResult, HashMap.class);
    }

    @PostMapping("/unfollow/{username}")
    @ResponseBody
    public String unfollowUser(@PathVariable String username,
                               HttpServletRequest request) {
        Map<String, Object> jsonResult = new HashMap<>();

        Optional<UserProfileViewModel> loggedUser = advice.getLoggedUser(request);
        Optional<User> userToUnfollow = userService.getUserByUsername(username);

        if (loggedUser.isPresent() && userToUnfollow.isPresent()) {
            User userProfileViewModel = modelMapper
                    .map(loggedUser.get(), User.class);

            //TODO
            userProfileViewModel.getFollowing().remove(userToUnfollow.get());

            jsonResult.put("success", true);
        } else {
            jsonResult.put("success", false);
        }

        return gson.toJson(jsonResult, HashMap.class);
    }
}
