package com.project.drivemodeon.model.service.user;

import lombok.Data;

@Data
public class UserServiceModel {
    private String username;

    private String firstName;

    private String lastName;

    private String bio;

    private String email;

    private String password;
//    private String newPassword;
//
//    private String currentPassword;

    private boolean isProfilePrivate;
}
