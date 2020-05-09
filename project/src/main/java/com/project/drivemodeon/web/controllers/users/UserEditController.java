package com.project.drivemodeon.web.controllers.users;

import com.project.drivemodeon.web.controllers.advices.Advice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/editprofile")
public class UserEditController {
    private final Advice adviceController;

    public UserEditController(Advice adviceController) {
        this.adviceController = adviceController;
    }

    @GetMapping
    @ResponseBody
    public String getUserSettingsPage(HttpServletRequest request) {
        String username = adviceController.getUsername(request);
        return username;
    }
}
