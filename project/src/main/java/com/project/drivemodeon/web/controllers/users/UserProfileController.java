package com.project.drivemodeon.web.controllers.users;

import com.google.gson.Gson;
import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.web.controllers.MainController;
import com.project.drivemodeon.web.controllers.advices.Advice;
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

    public UserProfileController(UserService userService, Gson gson, Advice advice) {
        this.userService = userService;
        this.gson = gson;
        this.advice = advice;
    }

    @GetMapping("/{username}")
    public ModelAndView getUserProfile(@PathVariable String username,
                                       HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("layouts/index");

        Optional<User> loggedUser = advice.getLoggedUser(request);
        Optional<User> currentPageUser = userService.getUserByUsername(username);

        if (currentPageUser.isPresent() && loggedUser.isPresent()) {
            modelAndView.addObject("view", "fragments/user/user_profile");
            modelAndView.addObject("profileId", currentPageUser.get().getId());
            modelAndView.addObject("profileUsername", username.toLowerCase());
            modelAndView.addObject("isUserFollowCurrentProfile",
                    userService.isCurrentUserFollowProfileUser(
                            loggedUser.get(), currentPageUser.get()));
            return modelAndView;
        }
        modelAndView.setViewName("fragments/errors/user/user_not_found");
        return modelAndView;
    }

    @PostMapping("/follow/{username}")
    @ResponseBody
    public String followUser(@PathVariable String username, HttpServletRequest request) {
        Map<String, Object> jsonResult = new HashMap<>();

        Optional<User> loggedUser = advice.getLoggedUser(request);
        Optional<User> followingUser = userService.getUserByUsername(username);

        boolean isUserFollowedSuccessfully = userService
                .followUser(loggedUser, followingUser);

        jsonResult.put("success", isUserFollowedSuccessfully);

        return gson.toJson(jsonResult, HashMap.class);
    }

    @PostMapping("/unfollow/{username}")
    @ResponseBody
    public String unfollowUser(@PathVariable String username,
                               HttpServletRequest request) {
        Map<String, Object> jsonResult = new HashMap<>();

        Optional<User> loggedUser = advice.getLoggedUser(request);
        Optional<User> userToUnfollow = userService.getUserByUsername(username);

        if (loggedUser.isPresent() && userToUnfollow.isPresent()) {
            loggedUser.get().getFollowing().remove(userToUnfollow.get());

            jsonResult.put("success", true);
        } else {
            jsonResult.put("success", false);
        }

        return gson.toJson(jsonResult, HashMap.class);
    }
}
