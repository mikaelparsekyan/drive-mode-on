package com.project.drivemodeon.model.service.comment;

import com.project.drivemodeon.model.entity.Post;
import com.project.drivemodeon.model.entity.User;
import lombok.Data;

@Data
public class CommentServiceModel {
    private User author;

    private String text;

    private Post post;
}
