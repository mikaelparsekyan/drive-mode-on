package com.project.drivemodeon.model.binding;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class UserSignUpBindingModel implements Serializable {
    //@UniqueUsername(message = "Username is already taken!")
    @NotNull(message = "Username cannot be empty!")
    @Length(min = 4, max = 10, message = "Username must be between 4 and 15 symbols!")
    private String username;

    //@UniqueEmail(message = "Email is already taken!")
    @NotNull(message = "Email cannot be empty!")
    @Email(message = "Invalid email address!")
    private String email;

    @NotNull(message = "Password cannot be empty!")
    @Length(min = 8, message = "Password must be at least 8 symbols!")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{1,}$",
            message = "Password must contains one upper letter, one lower letter and one digit!"
    )
    private String password;

    @NotNull
    private String confirmPassword;
}
