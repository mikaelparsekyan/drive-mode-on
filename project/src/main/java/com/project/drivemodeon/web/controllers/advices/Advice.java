package com.project.drivemodeon.web.controllers.advices;

import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.web.view_models.user.UserProfileViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
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
    public boolean isUserLoggedIn(HttpServletRequest request) {
        Long loggedUserId = (Long) request.getSession().getAttribute("user_id");

        return loggedUserId != null;
    }

    @ModelAttribute("username")
    public String getUsername(HttpServletRequest request) {
        Long loggedUserId = (Long) request.getSession().getAttribute("user_id");

        if (loggedUserId != null) {
            Optional<User> user = userService.getUserById(loggedUserId);
            if (user.isPresent()) {
                return user.get().getUsername().toLowerCase();
            }
        }
        return null;
    }

    @ModelAttribute("loggedUserId")
    public Long getLoggedUserId(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute("user_id");
    }

    @ModelAttribute("userProfileRoute")
    public String getUserProfileRoute(HttpServletRequest request) {
        Long loggedUserId = (Long) request.getSession().getAttribute("user_id");
        if (loggedUserId != null) {
            Optional<User> user = userService.getUserById(loggedUserId);
            if (user.isPresent()) {
                return String.format("/user/%s",
                        user.get().getUsername().toLowerCase());
            }
        }
        return null;
    }

    @ModelAttribute("sessionUser")
    public Optional<User> getLoggedUser(HttpServletRequest request) {
        Long loggedUserId = (Long) request.getSession().getAttribute("user_id");

        if (loggedUserId == null) {
            return Optional.empty();
        }
        Optional<User> user = userService.getUserById(loggedUserId);

        if (user.isEmpty()) {
            return Optional.empty();
        }

        return user;
    }

//    @ModelAttribute("isUserFollowCurrentProfile")
//    public boolean isUserFollowCurrentProfile(HttpServletRequest request) {
//        Optional<UserProfileViewModel> loggedUser = this.getLoggedUser(request);
//
//        if (loggedUser.isEmpty()) {
//            return false;
//        }
//
//        return loggedUser.get().getFollowers().contains());
//    }

}
