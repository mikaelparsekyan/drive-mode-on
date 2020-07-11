package com.project.drivemodeon.web.controller;

import com.google.gson.Gson;
import com.project.drivemodeon.model.binding.comment.AddCommentBindingModel;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.comment.CommentServiceModel;
import com.project.drivemodeon.service.api.comment.CommentService;
import com.project.drivemodeon.service.api.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/comment")
public class CommentController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final CommentService commentService;
    private final Gson gson;

    public CommentController(ModelMapper modelMapper, UserService userService, CommentService commentService, Gson gson) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.commentService = commentService;
        this.gson = gson;
    }

    @PostMapping("/add")
    @ResponseBody
    public String postComment(@AuthenticationPrincipal Principal principal, HttpServletRequest request) {
        String text = request.getParameter("text");
        System.out.println(text);
        Map<String, Object> jsonResult = new HashMap<>();
        jsonResult.put("success", false);

        if (principal == null) {
            return gson.toJson(jsonResult, HashMap.class);
        }

//        CommentServiceModel commentServiceModel = modelMapper
//                .map(addCommentBindingModel, CommentServiceModel.class);
//
//        User user = userService.getUserByUsername(principal.getName());
//
//        if (user != null) {
//            commentService.addComment(commentServiceModel, addCommentBindingModel.getPostId());
//        }
        jsonResult.put("success", true);
        return gson.toJson(jsonResult, HashMap.class);
    }
}
