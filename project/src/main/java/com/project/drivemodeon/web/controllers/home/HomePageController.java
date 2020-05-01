package com.project.drivemodeon.web.controllers.home;

import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class HomePageController extends MainController {
    private final UserService userService;

    public HomePageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/", "/index", "/home"})
    public ModelAndView getHomePage(HttpServletRequest request) {
        Long loggedUserId = (Long) request.getSession().getAttribute("user_id");

        String text = "";
        if (loggedUserId == null) {
            text = "no user logged";
        } else {
            Optional<User> userFromSession = userService.getUserById(loggedUserId);

            if (userFromSession.isPresent()) {
                text = userFromSession.get().getUsername();
            }
        }

        return super.view("fragments/home", "text", text);
    }
}
