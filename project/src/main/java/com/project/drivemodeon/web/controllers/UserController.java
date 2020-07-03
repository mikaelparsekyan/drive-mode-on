package com.project.drivemodeon.web.controllers;

import com.google.gson.Gson;
import com.project.drivemodeon.model.service.users.UserSignInDto;
import com.project.drivemodeon.model.service.users.UserSignUpDto;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.exception.user.InvalidUserSignUp;
import com.project.drivemodeon.exception.user.UserNotExistException;
import com.project.drivemodeon.services.api.user.UserService;
import com.project.drivemodeon.util.api.ValidatorUtil;
import com.project.drivemodeon.web.controllers.advice.Advice;
import com.project.drivemodeon.web.view_models.user.UserProfileViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserController extends MainController {

    private final UserService userService;
    private final ValidatorUtil validatorUtil;
    private final Advice advice;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public UserController(UserService userService, ValidatorUtil validatorUtil, Advice advice, ModelMapper modelMapper, Gson gson) {
        this.userService = userService;
        this.validatorUtil = validatorUtil;
        this.advice = advice;

        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @GetMapping("/signup")
    public ModelAndView getSignUpPage(HttpServletRequest request) {
        HttpSession userSession = request.getSession();
        Long userId = (Long) userSession.getAttribute("user_id");

        if (userId == null) {
            UserSignUpDto userSignUpDto = new UserSignUpDto();
            return super.view("fragments/signup",
                    "user", userSignUpDto);
        }

        return new ModelAndView("redirect:/");
    }

    @PostMapping("/signup")
    public ModelAndView doSignUp(@Valid @ModelAttribute("user") UserSignUpDto userSignUpDto,
                                 BindingResult bindingResult) throws Exception {
        Map<String, Object> inputErrors = new HashMap<>();

        if (!bindingResult.hasErrors()) {
            try {
                userService.signUpUser(userSignUpDto);
            } catch (InvalidUserSignUp invalidUserSignUp) {
                inputErrors = new HashMap<>();
                inputErrors.put("confirmPassword", "Passwords did not match");
            }
        } else {
            inputErrors = validatorUtil.violations(userSignUpDto);
        }

        return super.view("fragments/signup", "inputErrors", inputErrors);
    }

    @GetMapping("/signin")
    public ModelAndView getSignInPage(HttpServletRequest request) {
        HttpSession userSession = request.getSession();
        Long userId = (Long) userSession.getAttribute("user_id");

        if (userId == null) {
            UserSignInDto userSignInDto = new UserSignInDto();
            return super.view("fragments/signin",
                    "user", userSignInDto);
        }
        Optional<User> loggedUser = userService.getUserById(userId);
        return new ModelAndView("redirect:/user/" + loggedUser.get().getUsername());//TODO make Advice class here
    }

    @PostMapping("/signin")
    public ModelAndView doSignIn(@ModelAttribute("user") UserSignInDto userSignInDto,
                                 HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> inputErrors = new HashMap<>();

        ModelAndView modelAndView = new ModelAndView("layouts/index");
        modelAndView.addObject("view", "fragments/signin");

        long signedUserId = userService.signInUser(userSignInDto);
        boolean isUserSignedIn = signedUserId != -1;
        if (isUserSignedIn) {
            HttpSession userSession = request.getSession();
            userSession.setAttribute("user_id", signedUserId);

            return new ModelAndView("redirect:/user/" + userSignInDto.getUsername());
        } else {
            inputErrors.put("invalidInfo", "Invalid username or password!");
        }
        modelAndView.addObject("inputErrors", inputErrors);

        return modelAndView;
    }

    @GetMapping("/user/{username}")
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

    @PostMapping("/user/follow/{username}")
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

    @PostMapping("/user/unfollow/{username}")
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

    @GetMapping("/user/logout")
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
