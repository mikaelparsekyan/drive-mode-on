package com.project.drivemodeon.model.service.users;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
public class UserSignInDto {

    @NotNull
    @Max(255)
    private String username;

    @NotNull
    @Max(255)
    private String password;
}
