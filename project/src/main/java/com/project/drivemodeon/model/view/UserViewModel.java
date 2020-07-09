package com.project.drivemodeon.model.view;

import com.project.drivemodeon.model.entity.AuthorityEntity;
import com.project.drivemodeon.model.entity.User;
import lombok.Data;

import java.util.Set;

@Data
public class UserViewModel {
    private long id;

    private String username;

    private String email;

    private String password;

    private String confirmPassword;

    private boolean isProfilePrivate;

    private Set<AuthorityEntity> authorities;

    private Set<User> followers;

    private Set<User> following;
}
