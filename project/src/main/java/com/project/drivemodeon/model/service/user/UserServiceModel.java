package com.project.drivemodeon.model.service.user;

import com.project.drivemodeon.model.entity.AuthorityEntity;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@Getter
public class UserServiceModel {
    private long id;

    @NotNull(message = "Username cannot be empty!")
    @Length(min = 4, max = 10, message = "Username must be between 4 and 15 symbols!")
    private String username;

    @NotNull(message = "Email cannot be empty!")
    @Email(message = "Invalid email address!")
    private String email;

    private String firstName;

    private String lastName;

    @Length(min = 1, max = 40, message = "Bio should be between 1 and 40 symbols!")
    private String bio;

    @NotNull(message = "Password cannot be empty!")
    @Length(min = 8, message = "Password must be at least 8 symbols!")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{1,}$",
            message = "Password must contains one upper letter, one lower letter and one digit!"
    )
    private String password;

    private String confirmPassword;

    private boolean isAccountPrivate;

    private Set<AuthorityEntity> authorities;
}
