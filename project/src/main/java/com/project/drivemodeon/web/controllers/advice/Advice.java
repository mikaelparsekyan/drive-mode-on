package com.project.drivemodeon.web.controllers.advice;

import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.services.api.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public String getUsername(HttpSession httpSession) {
        Long loggedUserId = (Long) httpSession.getAttribute("user_id");

        if (loggedUserId != null) {
            Optional<User> user = userService.getUserById(loggedUserId);
            if (user.isPresent()) {
                return user.get().getUsername().toLowerCase();
            }
        }
        return null;
    }

    @ModelAttribute("loggedUserId")
    public Long getLoggedUserId(HttpSession httpSession) {
        return (Long) httpSession.getAttribute("user_id");
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

        return Optional.of(user);
    }
}
