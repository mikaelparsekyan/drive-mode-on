package com.project.drivemodeon.model.binding.user;

import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@Getter
public class EditUserBindingModel implements Serializable {
    private long id;

    @NotNull(message = "Username cannot be empty!")
    @Length(min = 4, max = 10, message = "Username must be between 4 and 15 symbols!")
    private String username;

    @Length(min = 2, max = 20, message = "First name should be between 2 and 20 symbols!")
    private String firstName;

    @Length(min = 2, max = 20, message = "Last name should be between 2 and 20 symbols!")
    private String lastName;


    @NotNull(message = "Email cannot be empty!")
    @Email(message = "Invalid email address!")
    private String email;

    @Length(min = 1, max = 40, message = "Bio should be between 1 and 40 symbols!")
    private String bio;

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
