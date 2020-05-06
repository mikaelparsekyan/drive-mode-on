package com.project.drivemodeon.web.controllers.advices;

import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.services.api.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@ControllerAdvice
public class Advice {
    private final UserService userService;

    public Advice(UserService userService) {
        this.userService = userService;
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
                return user.get().getUsername();
            }
        }

        return null;
    }
}
