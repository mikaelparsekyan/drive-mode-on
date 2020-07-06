package com.project.drivemodeon.model.service.user;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
public class UserSignInDto {
    private long id;

    @NotNull
    @Max(255)
    private String username;

    @NotNull
    @Max(255)
    private String password;
}
