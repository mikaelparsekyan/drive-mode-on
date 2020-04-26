package com.project.drivemodeon.web.controllers.home;

import com.project.drivemodeon.web.controllers.MainController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomePageController extends MainController {

    @GetMapping(value = {"/", "/index", "/home"})
    public ModelAndView getHomePage(ModelAndView modelAndView) {
        return super.view("fragments/home");
    }
}
