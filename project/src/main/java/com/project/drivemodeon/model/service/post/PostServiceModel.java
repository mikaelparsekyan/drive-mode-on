package com.project.drivemodeon.model.service.post;

import com.project.drivemodeon.model.entity.Comment;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostServiceModel {
    private long id;

    @Length(min = 1, max = 1000)
    private String value;

    @NotNull
    private PostPrivacyEnum postPrivacy;

    @NotNull
    private boolean isDraft;

    @NotNull
    private User author;

    @NotNull
    private LocalDateTime postedOn;

    private String location;

    private Set<User> likers;

    private Set<Comment> comments;
}
