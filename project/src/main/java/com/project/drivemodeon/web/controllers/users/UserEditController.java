package com.project.drivemodeon.web.controllers.users;

import com.project.drivemodeon.domain.dtos.users.UserEditDto;
import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.web.controllers.advices.Advice;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/edit/profile")
public class UserEditController {
    private final UserService userService;
    private final Advice advice;
    private final ModelMapper modelMapper;

    public UserEditController(UserService userService, Advice advice, ModelMapper modelMapper) {
        this.userService = userService;
        this.advice = advice;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ModelAndView getUserSettingsPage(HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("layouts/index");
        modelAndView.addObject("view", "fragments/user/edit_user_profile");
        return modelAndView;
    }

    @PostMapping
    public ModelAndView doUserProfileEdit(HttpServletRequest request) {
        Long loggedUserId = advice.getLoggedUserId(request);

        String usernameParameter = request.getParameter("username");
        
        userService.editUser(usernameParameter, loggedUserId);

        Optional<User> user = userService.getUserById(loggedUserId);
        if (user.isEmpty()) {
            //UserId not valid
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/user/" + usernameParameter);
    }
}
