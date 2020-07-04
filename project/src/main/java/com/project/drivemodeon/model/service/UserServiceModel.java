package com.project.drivemodeon.model.service;

import com.project.drivemodeon.model.entity.UserBio;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Data
public class UserServiceModel {
    @NotNull
    private String username;

    private String firstName;

    private String lastName;

    private UserBio bio;

    private String email;

    private String password;
}
