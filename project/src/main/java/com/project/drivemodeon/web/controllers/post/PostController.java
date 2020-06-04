package com.project.drivemodeon.web.controllers.post;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/add/post")
public class PostController {

    @GetMapping
    public ModelAndView getPostPage() {

        return null;
    }
}
