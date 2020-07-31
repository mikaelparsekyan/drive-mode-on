package com.project.drivemodeon.model.view;

import com.project.drivemodeon.model.entity.Comment;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class PostViewModel {
    private long id;

    private String value;

    private PostPrivacyEnum postPrivacy;

    private boolean isDraft;

    private User author;

    private Set<User> likers;

    private LocalDateTime postedOn;

    private String location;

    private String imagePath;

    private Set<Comment> comments = new LinkedHashSet<>();
}
