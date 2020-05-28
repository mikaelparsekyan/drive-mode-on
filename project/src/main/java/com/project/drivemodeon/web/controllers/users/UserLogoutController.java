package com.project.drivemodeon.web.controllers.users;

import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.web.controllers.advices.Advice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/logout")
public class UserLogoutController {
    private final Advice advice;

    public UserLogoutController(Advice advice) {
        this.advice = advice;
    }

    @GetMapping
    public ModelAndView logoutUser(HttpServletRequest request) {
        Optional<User> user = advice.getLoggedUser(request);

        if (user.isPresent()) {
            request.getSession().removeAttribute("user_id");
        }

        return new ModelAndView("redirect:/");
    }
}
