package com.project.drivemodeon.services.api;

import com.project.drivemodeon.domain.dtos.users.UserSignInDto;
import com.project.drivemodeon.domain.dtos.users.UserSignUpDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    boolean signUpUser(UserSignUpDto userSignUpDto);

    boolean signInUser(UserSignInDto userSignInDto);

    boolean isEmailTaken(String email);

    boolean isUsernameTaken(String username);
}
