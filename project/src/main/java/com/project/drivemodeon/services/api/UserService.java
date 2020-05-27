package com.project.drivemodeon.services.api;

import com.project.drivemodeon.domain.dtos.users.UserEditDto;
import com.project.drivemodeon.domain.dtos.users.UserSignInDto;
import com.project.drivemodeon.domain.dtos.users.UserSignUpDto;
import com.project.drivemodeon.domain.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
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

    void followUser(User loggedUser, User followingUser);

    void unfollowUser(User loggedUser, User followingUser);

    long getUserFollowersCount(User user);

    long getUserFollowingsCount(User user);

    void editUser(String username, Long userId);
}
