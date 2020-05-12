package com.project.drivemodeon.web.view_models.user;

import com.project.drivemodeon.domain.models.User;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserProfileViewModel {
    private long id;

    private String username;

    private String profilePicture;

    private String firstName;

    private String lastName;

    private String bio;

    private Set<User> followers = new HashSet<>();

    private Set<User> following = new HashSet<>();
}
