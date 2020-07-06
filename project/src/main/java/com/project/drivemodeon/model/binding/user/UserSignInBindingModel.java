package com.project.drivemodeon.model.binding.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserSignInBindingModel implements Serializable {
    @NotNull(message = "Enter username!")
    @Length(min = 4, max = 25, message = "Username must be between 4 and 25 symbols!")
    private String username;

    @NotNull(message = "Enter password!")
    @Length(min = 5, max = 25, message = "Password must be between 5 and 25 symbols!")
    private String password;
}
