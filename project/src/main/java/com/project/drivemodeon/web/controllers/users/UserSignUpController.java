package com.project.drivemodeon.web.controllers.users;

import com.project.drivemodeon.domain.dtos.users.UserSignUpDto;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.util.api.ValidatorUtil;
import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/signup")
public class UserSignUpController extends MainController {
    private final UserService userService;
    private final ValidatorUtil validatorUtil;

    public UserSignUpController(UserService userService, ValidatorUtil validatorUtil) {
        this.userService = userService;
        this.validatorUtil = validatorUtil;
    }

    @GetMapping
    public ModelAndView showSignUpContent() {
        UserSignUpDto userSignInDto = new UserSignUpDto();

        Map<String, Object> objects = new HashMap<>();
        objects.put("user", userSignInDto);

        return super.view("fragments/signup", objects);
    }

    @PostMapping
    public ModelAndView doSignUp(@ModelAttribute("user")
                                         UserSignUpDto userDto) {
        if (validatorUtil.isValid(userDto)) {
            userService.signUpUser(userDto);
        } else {

        }
        return super.view("fragments/signup");
    }
}
