package com.project.drivemodeon.web.controllers.home;

import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomePageController extends MainController {
    private final UserService userService;

    public HomePageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/", "/index", "/home"})
    public ModelAndView getHomePage(ModelAndView modelAndView,
                                    HttpServletRequest request) {
        Long loggedUserId = (Long) request.getSession().getAttribute("USER-ID");

        if (loggedUserId == null) {
            request.getSession().setAttribute("USER-ID", loggedUserId);
        }

        return super.view("fragments/home");
    }
}
