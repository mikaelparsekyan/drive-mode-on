package com.project.drivemodeon.web.controllers.users;

import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserProfileController extends MainController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ModelAndView getUserProfile(@PathVariable String username,
                                       HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("layouts/index");

        Optional<User> user = userService.getUserByUsername(username);

        if (user.isPresent()) {
            modelAndView.addObject("view", "fragments/user/user_profile");
            modelAndView.addObject("profileId", user.get().getId());
            modelAndView.addObject("profileUsername", username.toLowerCase());
            return modelAndView;
        }
        modelAndView.setViewName("fragments/errors/user/user_not_found");
        return modelAndView;
    }
}
