package com.project.drivemodeon.web.controllers;

import com.google.gson.Gson;
import com.project.drivemodeon.exception.user.UserNotExistException;
import com.project.drivemodeon.model.binding.UserSignInBindingModel;
import com.project.drivemodeon.model.binding.UserSignUpBindingModel;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.users.UserSignUpDto;
import com.project.drivemodeon.services.api.user.UserService;
import com.project.drivemodeon.util.api.ValidatorUtil;
import com.project.drivemodeon.web.controllers.advice.Advice;
import com.project.drivemodeon.web.view_models.user.UserProfileViewModel;
import org.modelmapper.ModelMapper;
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

        ModelAndView modelAndView = new ModelAndView("fragments/signup");

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
        ModelAndView modelAndView = new ModelAndView("fragments/signup");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userSignUpBindingModel", userSignUpBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userSignUpBindingModel", bindingResult);
            return modelAndView;
        }

        UserSignUpDto userSignUpDto = modelMapper.map(userSignUpBindingModel, UserSignUpDto.class);
        try {
            userService.signUpUser(userSignUpDto);
        } catch (Exception e) {
            return modelAndView;
        }

        return modelAndView;
    }

    @GetMapping("/signin")
    public ModelAndView getSignInPage(@RequestParam(value = "error", required = false) String error,
                                      Model model, HttpServletRequest request, HttpSession httpSession) {
        HttpSession userSession = request.getSession();
        Long userId = (Long) userSession.getAttribute("user_id");

        ModelAndView modelAndView = new ModelAndView("fragments/signin");

        if (!model.containsAttribute("userSignInBindingModel")) {
            model.addAttribute("userSignInBindingModel", new UserSignInBindingModel());
        }
        if (error != null) {
            System.out.println("ERROR!");
        }

        return new ModelAndView("fragments/signin");
    }

//    @PostMapping("/signin")
//    public ModelAndView doSignIn(@Valid @ModelAttribute("userSignInBindingModel")
//                                         UserSignInBindingModel userSignInBindingModel,
//                                 BindingResult bindingResult,
//                                 RedirectAttributes redirectAttributes,
//                                 HttpSession session) {
//
//        if (bindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("userSignInBindingModel", userSignInBindingModel);
//            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userSignInBindingModel",
//                    bindingResult);
//            return new ModelAndView("redirect:signin");
//        }
//
//        UserSignInDto userSignInDto = modelMapper.map(userSignInBindingModel, UserSignInDto.class);
//
//        UserSignInDto signedUser = userService.signInUser(userSignInDto);
//        boolean isUserSignedIn = signedUser != null;
//
//        if (!isUserSignedIn) {
//            //model.addAttribute("notFound", true);
//            return new ModelAndView("fragments/signin").addObject("notFound", true);
//        }
//
//        session.setAttribute("user_id", signedUser.getId());
//        return new ModelAndView("redirect:/user/" + userSignInBindingModel.getUsername());
//    }

    @GetMapping("/user/{username}")
    public ModelAndView getUserProfile(@PathVariable String username,
                                       @AuthenticationPrincipal Principal principal,
                                       HttpSession httpSession) throws Exception {
        Optional<User> loggedUser = advice.getLoggedUser(principal);
        User currentPageUser = userService.getUserByUsername(username);

        ModelAndView modelAndView = new ModelAndView("fragments/user_profile");

        //TODO map other fields (bio, f_name, l_name ...)
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

    @ExceptionHandler(UserNotExistException.class)
    public ModelAndView getUserNotFoundPage(HttpServletResponse response) {
        response.setStatus(404);
        return super.view("fragments/errors/user/user_not_found");
    }
}
