package com.project.drivemodeon.web.controllers.users;

import com.project.drivemodeon.domain.dtos.users.UserSignInDto;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/signin")
public class UserSignInController extends MainController {
    private final UserService userService;

    public UserSignInController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView get() {
        UserSignInDto userSignInDto = new UserSignInDto();
        return super.view("fragments/signin",
                "user", userSignInDto);
    }

    @PostMapping
    public ModelAndView doSignIn(@ModelAttribute("user") UserSignInDto userSignInDto) {
        Map<String, Object> inputErrors = new HashMap<>();

        boolean isUserSignedIn = userService.signInUser(userSignInDto);
        if (isUserSignedIn) {
            System.out.println("USER IS SIGNED IN SUCCESSFULLY!");
        } else {
            inputErrors.put("invalidInfo", "Invalid username or password!");
        }

        return super.view("fragments/signin", "inputErrors",
                inputErrors);
    }
}
