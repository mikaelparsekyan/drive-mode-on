package com.project.drivemodeon.web.controllers.users;

import com.project.drivemodeon.domain.dtos.users.UserSignInDto;
import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/signin")
public class UserSignInController extends MainController {

    @GetMapping
    public ModelAndView get() {
        UserSignInDto userSignInDto = new UserSignInDto();
        return super.view("fragments/signin",
                "user", userSignInDto);

    }
}
