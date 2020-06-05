package com.project.drivemodeon.web.controllers.user;

import com.google.gson.Gson;
import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.exceptions.user.UserNotExistException;
import com.project.drivemodeon.services.api.user.UserService;
import com.project.drivemodeon.web.controllers.MainController;
import com.project.drivemodeon.web.controllers.advices.Advice;
import com.project.drivemodeon.web.view_models.user.UserProfileViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                                       HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView("layouts/index");

        Optional<User> loggedUser = advice.getLoggedUser(request);
        User currentPageUser = userService.getUserByUsername(username);

        //TODO map other fields (bio, f_name, l_name ...)
        modelAndView.addObject("view", "fragments/user/user_profile");
        modelAndView.addObject("userViewModel", currentPageUser);

        if (loggedUser.isPresent()) {
            UserProfileViewModel loggedUserViewModel = modelMapper
                    .map(loggedUser.get(), UserProfileViewModel.class);

            User sessionUser = modelMapper.map(loggedUserViewModel, User.class);

            modelAndView.addObject("isUserFollowCurrentProfile",
                    userService.isCurrentUserFollowProfileUser(
                            sessionUser, currentPageUser));
        }
        return modelAndView;
    }

    @PostMapping("/follow/{username}")
    @ResponseBody
    public String followUser(@PathVariable String username, HttpServletRequest request) throws Exception {
        Map<String, Object> jsonResult = new HashMap<>();

        Optional<User> loggedUser = advice.getLoggedUser(request);
        User followingUser = userService.getUserByUsername(username);

        if (loggedUser.isPresent()) {

            userService.followUser(loggedUser.get(), followingUser);

            jsonResult.put("success", true);
        } else {
            jsonResult.put("success", false);
        }

        return gson.toJson(jsonResult, HashMap.class);
    }

    @PostMapping("/unfollow/{username}")
    @ResponseBody
    public String unfollowUser(@PathVariable String username,
                               HttpServletRequest request) throws Exception {
        Map<String, Object> jsonResult = new HashMap<>();

        Optional<User> loggedUser = advice.getLoggedUser(request);
        User userToUnfollow = userService.getUserByUsername(username);

        if (loggedUser.isPresent()) {

            userService.unfollowUser(loggedUser.get(), userToUnfollow);

            jsonResult.put("success", true);
        } else {
            jsonResult.put("success", false);
        }

        return gson.toJson(jsonResult, HashMap.class);
    }

    @GetMapping("/logout")
    public ModelAndView logoutUser(HttpServletRequest request) {
        Optional<User> loggedUser = advice.getLoggedUser(request);

        if (loggedUser.isPresent()) {
            request.getSession().removeAttribute("user_id");
        }

        return new ModelAndView("redirect:/home");
    }

    @ExceptionHandler(UserNotExistException.class)
    public ModelAndView getUserNotFoundPage(HttpServletResponse response) {
        response.setStatus(404);
        return super.view("fragments/errors/user/user_not_found");
    }
}
