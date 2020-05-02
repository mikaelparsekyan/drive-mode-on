package com.project.drivemodeon.web.controllers.users;

import com.project.drivemodeon.domain.dtos.users.UserSignUpDto;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.util.api.ValidatorUtil;
import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public ModelAndView getSignUpPage(HttpServletRequest request) {
        HttpSession userSession = request.getSession();
        Long userId = (Long) userSession.getAttribute("user_id");

        if (userId == null) {
            UserSignUpDto userSignUpDto = new UserSignUpDto();
            return super.view("fragments/signup",
                    "user", userSignUpDto);
        }

        return new ModelAndView("redirect:/");
    }

    @PostMapping
    public ModelAndView doSignUp(@Valid @ModelAttribute("user") UserSignUpDto userSignUpDto,
                                 BindingResult bindingResult) {
        Map<String, Object> inputErrors = new HashMap<>();

        if (!bindingResult.hasErrors()) {
            boolean isUserSignedUp = userService.signUpUser(userSignUpDto);
            if (!isUserSignedUp) {
                inputErrors = new HashMap<>();
                inputErrors.put("confirmPassword", "Passwords did not match");
            }
        } else {
            inputErrors = validatorUtil.violations(userSignUpDto);
        }

        return super.view("fragments/signup", "inputErrors",
                inputErrors);
    }
}
