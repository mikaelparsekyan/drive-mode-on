package com.project.drivemodeon.services.api;

import com.project.drivemodeon.domain.dtos.users.UserSignUpDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    boolean signUpUser(UserSignUpDto userSignUpDto);

    boolean isEmailTaken(String email);

    boolean isNicknameTaken(String value);
}
