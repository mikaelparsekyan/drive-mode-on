package com.project.drivemodeon.web.controller;

import com.google.gson.Gson;
import com.project.drivemodeon.model.binding.comment.AddCommentBindingModel;
import com.project.drivemodeon.model.entity.Comment;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.comment.CommentServiceModel;
import com.project.drivemodeon.model.service.post.PostServiceModel;
import com.project.drivemodeon.service.api.comment.CommentService;
import com.project.drivemodeon.service.api.post.PostService;
import com.project.drivemodeon.service.api.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/comment")
public class CommentController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final Gson gson;

    public CommentController(ModelMapper modelMapper, UserService userService, PostService postService, CommentService commentService, Gson gson) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.gson = gson;
    }

    @PostMapping("/add")
    @ResponseBody
    @Transactional
    public String postComment(@Valid @ModelAttribute AddCommentBindingModel addCommentBindingModel,
                              @AuthenticationPrincipal Principal principal) {
        System.out.println();
        Map<String, Object> jsonResult = new HashMap<>();
        jsonResult.put("success", false);

        if (principal == null) {
            return gson.toJson(jsonResult, HashMap.class);
        }

        CommentServiceModel commentServiceModel = modelMapper
                .map(addCommentBindingModel, CommentServiceModel.class);

        User user = userService.getUserByUsername(principal.getName());

        if (user != null) {
            commentServiceModel.setAuthor(user);
            commentService.addComment(commentServiceModel, addCommentBindingModel.getPostId());
        }
        jsonResult.put("success", true);
        return gson.toJson(jsonResult, HashMap.class);
    }

    @GetMapping("/get/{postId}")
    @ResponseBody
    public String getComments(@PathVariable("postId") Long id) {
        Map<String, Object> jsonResult = new HashMap<>();
        if (id == null) {
            jsonResult.put("success", false);
        }
        Optional<PostServiceModel> postById = postService.getPostById(id);

        if (postById.isEmpty()) {
            jsonResult.put("success", false);
        }
        Map<String, Map<String, String>> commentsSet = new LinkedHashMap<>();

        LinkedHashSet<Comment> postComments = postById.get().getComments().stream()
                .sorted(Comparator.comparing(Comment::getDate))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        int i = 1;
        for (Comment comment : postComments) {
            Map<String, String> commentData = new LinkedHashMap<>();
            commentData.put("text", comment.getText());
            commentData.put("author", comment.getAuthor().getUsername());
            commentsSet.put("comment" + (i++), commentData);
        }
        jsonResult.put("success", true);
        jsonResult.put("comments", commentsSet);
        return gson.toJson(jsonResult);
    }
}
