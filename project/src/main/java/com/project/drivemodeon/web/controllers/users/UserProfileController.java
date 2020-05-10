package com.project.drivemodeon.web.controllers.users;

import com.google.gson.Gson;
import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserProfileController extends MainController {

    private final UserService userService;

    private final Gson gson;

    public UserProfileController(UserService userService, Gson gson) {
        this.userService = userService;
        this.gson = gson;
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

    @PostMapping("/follow/{username}")
    @ResponseBody
    public String followUser(@PathVariable String username) {
        Map<String, Object> jsonStr = new HashMap<>();
        jsonStr.put("success", true);
        jsonStr.put("username", username);


        return gson.toJson(jsonStr, HashMap.class);
    }
}
