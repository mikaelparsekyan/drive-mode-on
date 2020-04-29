package com.project.drivemodeon.domain.dtos.users;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
public class UserSignInDto {

    @NotNull
    @Max(255)
    private String usernameOrEmail;

    @NotNull
    @Max(255)
    private String password;
}
