package com.project.drivemodeon.web.controller;

import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.service.api.user.UserService;
import com.project.drivemodeon.web.controller.advice.Advice;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/edit/profile")
public class UserEditController {
    private final UserService userService;
    private final Advice advice;
    private final ModelMapper modelMapper;

    public UserEditController(UserService userService, Advice advice, ModelMapper modelMapper) {
        this.userService = userService;
        this.advice = advice;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ModelAndView getUserSettingsPage() {

        ModelAndView modelAndView = new ModelAndView("layouts/index");
        modelAndView.addObject("view", "fragments/user/edit_user_profile");
        return modelAndView;
    }

    @PostMapping
    public ModelAndView doUserProfileEdit(HttpSession httpSession,
                                          HttpServletRequest request,
                                          @AuthenticationPrincipal Principal principal) {
        Long loggedUserId = advice.getLoggedUserId(principal);

        //TODO make obj
        String usernameParameter = request.getParameter("username");

        userService.editUser(usernameParameter, loggedUserId);

        Optional<User> user = userService.getUserById(loggedUserId);
        if (user.isEmpty()) {
            //UserId not valid
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/user/" + usernameParameter);
    }

    @GetMapping("/uploadImage")
    public ModelAndView getEditUserForm() {
        return getUserSettingsPage();
    }

    @PostMapping("/uploadImage")
    public ModelAndView uploadImage(@RequestParam("imageFile") MultipartFile imageFile) {
        System.out.println("posting image...");
        String path = "src/main/resources/photos/user/";
        try {
            Files.write(Path.of(path + imageFile.getOriginalFilename()), imageFile.getBytes());
        } catch (MaxUploadSizeExceededException | IOException e) {
            System.out.println("too large file! :(");
        }
        return getUserSettingsPage();
    }
}
