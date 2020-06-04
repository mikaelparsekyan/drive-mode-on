package com.project.drivemodeon.services.api.user;

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

    User getUserByUsername(String username) throws Exception;

    boolean isCurrentUserFollowProfileUser(User currentUser, User profileUser);

    void followUser(User loggedUser, User followingUser);

    void unfollowUser(User loggedUser, User followingUser);

    long getUserFollowersCount(User user);

    long getUserFollowingsCount(User user);

    void editUser(String username, Long userId);
}
