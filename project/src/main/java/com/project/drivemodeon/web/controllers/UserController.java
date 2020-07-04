package com.project.drivemodeon.web.controllers;

import com.google.gson.Gson;
import com.project.drivemodeon.exception.user.InvalidUserSignUp;
import com.project.drivemodeon.exception.user.UserNotExistException;
import com.project.drivemodeon.model.binding.UserSignInBindingModel;
import com.project.drivemodeon.model.binding.UserSignUpBindingModel;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.users.UserSignInDto;
import com.project.drivemodeon.model.service.users.UserSignUpDto;
import com.project.drivemodeon.services.api.user.UserService;
import com.project.drivemodeon.util.api.ValidatorUtil;
import com.project.drivemodeon.web.controllers.advice.Advice;
import com.project.drivemodeon.web.view_models.user.UserProfileViewModel;
import org.modelmapper.ModelMapper;
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
    public String getSignUpPage(Model model,
                                HttpSession httpSession) {
        Long userId = (Long) httpSession.getAttribute("user_id");

        if (!model.containsAttribute("userSignUpBindingModel")) {
            model.addAttribute("userSignUpBindingModel", new UserSignUpBindingModel());
        }

        if (userId == null) {
            return "fragments/signup";
        }

        return "redirect:signin";
    }

    @PostMapping("/signup")
    public String doSignUp(@Valid @ModelAttribute("userSignUpBindingModel")
                                   UserSignUpBindingModel userSignUpBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userSignUpBindingModel", userSignUpBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userSignUpBindingModel", bindingResult);
            return "redirect:signup";
        }

        UserSignUpDto userSignUpDto = modelMapper.map(userSignUpBindingModel, UserSignUpDto.class);
        try {
            userService.signUpUser(userSignUpDto);
        } catch (Exception e) {
            return "redirect:signup";
        }

        return "fragments/signup";
    }

    @GetMapping("/signin")
    public String getSignInPage(Model model, HttpServletRequest request) {
        HttpSession userSession = request.getSession();
        Long userId = (Long) userSession.getAttribute("user_id");

        if (!model.containsAttribute("userSignInBindingModel")) {
            model.addAttribute("userSignInBindingModel", new UserSignInBindingModel());
        }

        if (userId == null) {
            return "fragments/signin";
        }

        Optional<User> loggedUser = userService.getUserById(userId);
        return ("redirect:/user/" + loggedUser.get().getUsername());//TODO make Advice class here
    }

    @PostMapping("/signin")
    public String doSignIn(@Valid @ModelAttribute("userSignInBindingModel")
                                   UserSignInBindingModel userSignInBindingModel,
                           Model model,
                           HttpSession session) {


        UserSignInDto userSignInDto = modelMapper.map(userSignInBindingModel, UserSignInDto.class);

        UserSignInDto signedUser = userService.signInUser(userSignInDto);
        boolean isUserSignedIn = signedUser != null;

        if (!isUserSignedIn) {
            model.addAttribute("notFound", true);
            return "fragments/signin";
        }

        session.setAttribute("user_id", signedUser.getId());
        return "redirect:/user/" + userSignInBindingModel.getUsername();
    }

    @GetMapping("/user/{username}")
    public String getUserProfile(@PathVariable String username,
                                 Model model,
                                 HttpServletRequest request) throws Exception {
        Optional<User> loggedUser = advice.getLoggedUser(request);
        User currentPageUser = userService.getUserByUsername(username);

        //TODO map other fields (bio, f_name, l_name ...)
        model.addAttribute("userViewModel", currentPageUser);

        if (loggedUser.isPresent()) {
            UserProfileViewModel loggedUserViewModel = modelMapper
                    .map(loggedUser.get(), UserProfileViewModel.class);

            User sessionUser = modelMapper.map(loggedUserViewModel, User.class);

            model.addAttribute("isUserFollowCurrentProfile",
                    userService.isCurrentUserFollowProfileUser(
                            sessionUser, currentPageUser));
        }
        return "fragments/user_profile";
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
