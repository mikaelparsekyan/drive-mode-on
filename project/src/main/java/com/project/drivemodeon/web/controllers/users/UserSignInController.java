package com.project.drivemodeon.web.controllers.users;

import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/signin")
public class UserSignInController extends MainController {

    @GetMapping
    @ResponseBody
    public String get(){
        return "sign in";
    }
}
