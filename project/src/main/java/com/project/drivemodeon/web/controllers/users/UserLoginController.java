package com.project.drivemodeon.web.controllers.users;

import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login")
public class UserLoginController extends MainController {

    @GetMapping
    public ModelAndView doLogin() {
        return super.view("fragments/login");
    }
}
