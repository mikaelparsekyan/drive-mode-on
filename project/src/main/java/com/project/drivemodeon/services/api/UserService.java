package com.project.drivemodeon.services.api;

import com.project.drivemodeon.domain.dtos.users.UserSignInDto;
import com.project.drivemodeon.domain.dtos.users.UserSignUpDto;
import com.project.drivemodeon.domain.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    boolean signUpUser(UserSignUpDto userSignUpDto);

    long signInUser(UserSignInDto userSignInDto);

    boolean isEmailTaken(String email);

    boolean isUsernameTaken(String username);

    Optional<User> getUserById(long id);

    Optional<User> getUserByUsername(String username);

    boolean isCurrentUserFollowProfileUser(User currentUser, User profileUser);

    boolean followUser(Optional<User> loggedUser, Optional<User> followingUser);
}
