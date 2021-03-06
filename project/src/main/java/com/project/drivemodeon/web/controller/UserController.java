package com.project.drivemodeon.web.controller;

import com.google.gson.Gson;
import com.project.drivemodeon.exception.user.UserNotExistException;
import com.project.drivemodeon.exception.user.UserNotLoggedException;
import com.project.drivemodeon.exception.user.signup.BaseSignUpException;
import com.project.drivemodeon.model.binding.comment.AddCommentBindingModel;
import com.project.drivemodeon.model.binding.user.EditUserBindingModel;
import com.project.drivemodeon.model.binding.user.UserSignInBindingModel;
import com.project.drivemodeon.model.binding.user.UserSignUpBindingModel;
import com.project.drivemodeon.model.entity.Post;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.model.view.UserViewModel;
import com.project.drivemodeon.service.api.user.UserService;
import com.project.drivemodeon.util.api.ValidatorUtil;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import com.project.drivemodeon.web.controller.advice.Advice;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

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
        User loggedUser = null;
        if (principal != null) {
            loggedUser = userService.getUserByUsername(principal.getName());
        }
        User currentPageUser = userService.getUserByUsername(username);

        if (currentPageUser == null) {
            throw new UserNotExistException();
        }

        ModelAndView modelAndView = new ModelAndView("user_profile");
        modelAndView.addObject("userViewModel", currentPageUser);
        modelAndView.addObject("addCommentBindingModel", new AddCommentBindingModel());

        Set<Post> posts = currentPageUser.getPosts();
        if (loggedUser == null) {
            posts = posts.stream().filter(post -> post.getPostPrivacy() == PostPrivacyEnum.PUBLIC)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            modelAndView.addObject("userPosts", posts);

            modelAndView.addObject("isUserFollowCurrentProfile", false);
        } else {
            final User user = loggedUser;
            posts = posts.stream().filter(post -> post.getPostPrivacy() == PostPrivacyEnum.PUBLIC && !post.isDraft() || (
                    post.getPostPrivacy() == PostPrivacyEnum.FOR_FRIENDS_ONLY && post.getAuthor().getFollowers()
                            .contains(user) && !post.isDraft()) || (post.getAuthor().getId() == user.getId() && !post.isDraft()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            modelAndView.addObject("userPosts", posts);

            UserViewModel loggedUserViewModel = modelMapper
                    .map(loggedUser, UserViewModel.class);

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

    @GetMapping("/user/edit/profile")
    public ModelAndView getUserSettingsPage(Model model, @AuthenticationPrincipal Principal principal) {
        ModelAndView modelAndView = new ModelAndView("redirect:/");

        if (principal == null) {
            return modelAndView;
        }

        User userByUsername = userService.getUserByUsername(principal.getName());

        if (userByUsername == null) {
            return modelAndView;
        }

        if (!model.containsAttribute("editUserBindingModel")) {
            EditUserBindingModel editUserBindingModel = modelMapper
                    .map(userByUsername, EditUserBindingModel.class);
            editUserBindingModel.setIsAccountPrivate(userByUsername.isAccountPrivate() ? 1 : 0);
            editUserBindingModel.setPassword("");//reset password
            model.addAttribute("editUserBindingModel", editUserBindingModel);
        }
        modelAndView.setViewName("edit_profile");
        return modelAndView;
    }

    @PostMapping("/edit/profile/uploadImage")
    public ModelAndView uploadProfileImage(@AuthenticationPrincipal Principal principal,
                                           @RequestParam("imageFile") MultipartFile image) throws Exception {
        if (principal == null) {
            throw new UserNotLoggedException();
        }
        User user = userService.getUserByUsername(principal.getName());
        if (user == null) {
            throw new UserNotExistException();
        }

        userService.uploadProfileImage(image, user);

        return new ModelAndView("redirect:/user/" + user.getUsername());
    }

    @PostMapping("/user/edit/profile")
    public ModelAndView doUserProfileEdit(@Valid @ModelAttribute("editUserBindingModel")
                                                  EditUserBindingModel editUserBindingModel,
                                          BindingResult bindingResult,
                                          RedirectAttributes redirectAttributes,
                                          @AuthenticationPrincipal Principal principal) {
        ModelAndView modelAndView = new ModelAndView("redirect:/user/edit/profile");
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editUserBindingModel", editUserBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editUserBindingModel", bindingResult);
            return modelAndView;
        }

        if (principal == null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        Long loggedUserId = advice.getLoggedUserId(principal);

        UserServiceModel userServiceModel = modelMapper.map(editUserBindingModel, UserServiceModel.class);

        if (!userServiceModel.getPassword().equals(userServiceModel.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("passNotMatch", true);
            return modelAndView;
        }
        if (!userServiceModel.getUsername().equals(advice.getLoggedUser(principal).get().getUsername()) &&
                userService.isUsernameTaken(userServiceModel.getUsername())) {
            redirectAttributes.addFlashAttribute("usernameIsTaken", true);
            return modelAndView;
        } else if (!userServiceModel.getEmail().equals(advice.getLoggedUser(principal).get().getEmail()) &&
                userService.isEmailTaken(userServiceModel.getEmail())) {
            redirectAttributes.addFlashAttribute("emailIsTaken", true);
            return modelAndView;
        }

        userServiceModel.setId(loggedUserId);
        userServiceModel.setAccountPrivate(editUserBindingModel.getIsAccountPrivate() == 1);
        userService.editUser(userServiceModel);
        //Authenticate user after edit
        Collection<SimpleGrantedAuthority> nowAuthorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
                .getContext().getAuthentication().getAuthorities();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userServiceModel.getUsername(), userServiceModel.getPassword(),
                nowAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        modelAndView.setViewName("redirect:/user/" + userServiceModel.getUsername());
        return modelAndView;
    }
}
