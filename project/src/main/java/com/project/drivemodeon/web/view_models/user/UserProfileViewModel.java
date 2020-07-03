package com.project.drivemodeon.web.view_models.user;

import com.project.drivemodeon.model.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class UserProfileViewModel {
    private long id;

    private String username;

    private String profilePicture;

    private String firstName;

    private String lastName;

    private String bio;

    private Set<User> followers;

    private Set<User> following;

    private boolean isAccountPrivate;
}
