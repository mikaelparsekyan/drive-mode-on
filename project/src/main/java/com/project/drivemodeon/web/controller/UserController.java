package com.project.drivemodeon.web.controller;

import com.google.gson.Gson;
import com.project.drivemodeon.exception.user.UserNotExistException;
import com.project.drivemodeon.exception.user.signup.BaseSignUpException;
import com.project.drivemodeon.model.binding.user.UserSignInBindingModel;
import com.project.drivemodeon.model.binding.user.UserSignUpBindingModel;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.model.service.user.UserSignUpDto;
import com.project.drivemodeon.service.api.user.UserService;
import com.project.drivemodeon.util.api.ValidatorUtil;
import com.project.drivemodeon.web.controller.advice.Advice;
import com.project.drivemodeon.web.view_models.user.UserProfileViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
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
    public ModelAndView getSignUpPage(Model model,
                                      HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("user_id");

        ModelAndView modelAndView = new ModelAndView("signup");

        if (!model.containsAttribute("userSignUpBindingModel")) {
            model.addAttribute("userSignUpBindingModel", new UserSignUpBindingModel());
        }

        if (userId == null) {
            return modelAndView;
        }

        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView doSignUp(@Valid @ModelAttribute("userSignUpBindingModel")
                                         UserSignUpBindingModel userSignUpBindingModel,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("signup");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userSignUpBindingModel", userSignUpBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userSignUpBindingModel", bindingResult);
            return modelAndView;
        }

        UserServiceModel userServiceModel = modelMapper
                .map(userSignUpBindingModel, UserServiceModel.class);
        try {
            userService.signUpUser(userServiceModel);
        } catch (BaseSignUpException e) {
            modelAndView.addObject(e.getFieldName() + "Err", e.getMessage());
            return modelAndView;
        }

        return new ModelAndView("redirect:/signin");
    }

    @GetMapping("/signin")
    public ModelAndView getSignInPage(@RequestParam(value = "error", required = false) String error,
                                      Model model) {

        ModelAndView modelAndView = new ModelAndView("signin");

        if (!model.containsAttribute("userSignInBindingModel")) {
            model.addAttribute("userSignInBindingModel", new UserSignInBindingModel());
        }
        if (error != null) {
            modelAndView.addObject("wrongInputData", true);
        }
        return modelAndView;
    }

    @GetMapping("/user/{username}")
    public ModelAndView getUserProfile(@PathVariable String username,
                                       @AuthenticationPrincipal Principal principal) throws Exception {
        Optional<User> loggedUser = advice.getLoggedUser(principal);
        User currentPageUser = userService.getUserByUsername(username);

        if (currentPageUser == null) {
            throw new UserNotExistException();
        }

        ModelAndView modelAndView = new ModelAndView("user_profile");

        //TODO map other fields (bio, f_name, l_name ...)
        modelAndView.addObject("userViewModel", currentPageUser);


        UserProfileViewModel loggedUserViewModel = modelMapper
                .map(loggedUser.get(), UserProfileViewModel.class);

        User sessionUser = modelMapper.map(loggedUserViewModel, User.class);

        modelAndView.addObject("isUserFollowCurrentProfile",
                userService.isCurrentUserFollowProfileUser(
                        sessionUser, currentPageUser));

        return modelAndView;
    }

    @PostMapping("/user/follow/{username}")
    @ResponseBody
    public String followUser(@PathVariable String username,
                             @AuthenticationPrincipal Principal principal,
                             HttpSession httpSession) throws Exception {
        Map<String, Object> jsonResult = new HashMap<>();

        Optional<User> loggedUser = advice.getLoggedUser(principal);
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
                               @AuthenticationPrincipal Principal principal,
                               HttpSession httpSession) throws Exception {
        Map<String, Object> jsonResult = new HashMap<>();

        Optional<User> loggedUser = advice.getLoggedUser(principal);
        User userToUnfollow = userService.getUserByUsername(username);

        if (loggedUser.isPresent()) {

            userService.unfollowUser(loggedUser.get(), userToUnfollow);

            jsonResult.put("success", true);
        } else {
            jsonResult.put("success", false);
        }

        return gson.toJson(jsonResult, HashMap.class);
    }

    @GetMapping("/user/edit/profile")
    public ModelAndView getUserSettingsPage() {

        return new ModelAndView("edit_profile");
    }

    @PostMapping("/user/edit/profile")
    public ModelAndView doUserProfileEdit(HttpServletRequest request,
                                          @AuthenticationPrincipal Principal principal) {
        Long loggedUserId = advice.getLoggedUserId(principal);

        //TODO make obj
        String usernameParameter = request.getParameter("username");

        userService.editUser(usernameParameter, loggedUserId);

        Optional<User> user = userService.getUserById(loggedUserId);
        if (user.isEmpty()) {
            //UserId not valid
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/user/" + usernameParameter);
    }
}
