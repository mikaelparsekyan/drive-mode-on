package com.project.drivemodeon.web.controller.advice;

import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.view.UserViewModel;
import com.project.drivemodeon.service.api.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Optional;

@ControllerAdvice
public class Advice {
    //TODO remove code duplication
    private final UserService userService;

    private final ModelMapper modelMapper;

    public Advice(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("isUserLogged")
    public boolean isUserLoggedIn(@AuthenticationPrincipal Principal principal) {
        return principal != null;
    }

    @ModelAttribute("username")
    public String getUsername(@AuthenticationPrincipal Principal principal) {
        if (principal != null) {
            return principal.getName();
        }
        return null;
    }

    @ModelAttribute("loggedUserId")
    public Long getLoggedUserId(@AuthenticationPrincipal Principal principal) {
        if (principal != null) {
            User user = userService.getUserByUsername(principal.getName());
            if (user == null) {
                return null;
            }
            return user.getId();
        }
        return null;
    }

    @ModelAttribute("userProfileRoute")
    public String getUserProfileRoute(@AuthenticationPrincipal Principal principal) {
        if (principal != null) {
            String name = principal.getName();
            if (name != null) {
                User user = userService.getUserByUsername(name);
                if (user != null) {//TODO make optional all this types
                    return String.format("/user/%s",
                            user.getUsername().toLowerCase());
                }
            }
        }
        return null;
    }

    @ModelAttribute("sessionUser")
    public Optional<User> getLoggedUser(@AuthenticationPrincipal Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }
        User user = userService.getUserByUsername(principal.getName());

        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
