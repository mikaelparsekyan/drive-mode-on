package com.project.drivemodeon.web.controllers.users;

import com.project.drivemodeon.domain.dtos.users.UserSignInDto;
import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/signin")
public class UserSignInController extends MainController {
    private final UserService userService;

    public UserSignInController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getSignInPage(HttpServletRequest request) {
        HttpSession userSession = request.getSession();
        Long userId = (Long) userSession.getAttribute("user_id");

        if (userId == null) {
            UserSignInDto userSignInDto = new UserSignInDto();
            return super.view("fragments/signin",
                    "user", userSignInDto);
        }
        Optional<User> loggedUser = userService.getUserById(userId);
        return new ModelAndView("redirect:/user/" + loggedUser.get().getUsername());
    }

    @PostMapping
    public ModelAndView doSignIn(@ModelAttribute("user") UserSignInDto userSignInDto,
                                 HttpServletRequest request) {
        Map<String, Object> inputErrors = new HashMap<>();

        ModelAndView modelAndView = new ModelAndView("layouts/index");
        modelAndView.addObject("view", "fragments/signin");

        long signedUserId = userService.signInUser(userSignInDto);
        boolean isUserSignedIn = signedUserId != -1;
        if (isUserSignedIn) {
            HttpSession userSession = request.getSession();
            userSession.setAttribute("user_id", signedUserId);
        } else {
            inputErrors.put("invalidInfo", "Invalid username or password!");
        }
        modelAndView.addObject("inputErrors", inputErrors);

        return modelAndView;
    }
}
