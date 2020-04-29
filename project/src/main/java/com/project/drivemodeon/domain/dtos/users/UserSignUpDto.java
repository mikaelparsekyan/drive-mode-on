package com.project.drivemodeon.domain.dtos.users;

import com.project.drivemodeon.validation.annotations.UniqueEmail;
import com.project.drivemodeon.validation.annotations.UniqueUsername;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserSignUpDto {

    @UniqueUsername(message = "Username is already taken!")
    @NotNull(message = "Username cannot be empty!")
    @Size(min = 4, max = 10, message = "Username must be between 4 and 15 symbols!")
    private String username;

    @UniqueEmail(message = "Email is already taken!")
    @NotNull(message = "Email cannot be empty!")
    @Pattern(
            regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
            message = "Invalid email address!")
    private String email;

    @NotNull(message = "Password cannot be empty!")
    @Size(min = 8, message = "Password must be at least 8 symbols!")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{1,}$",
            message = "Password must contains one upper letter, one lower letter and one digit!"
    )
    private String password;

    @NotNull
    private String confirmPassword;
}
