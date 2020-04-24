package com.project.drivemodeon.domain.dtos.users;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserSignUpDto {
    @NotNull
    @Size(min = 4, max = 10)
    private String nickname;

    @NotNull
    private String email;

    @NotNull
    private String password;
}
