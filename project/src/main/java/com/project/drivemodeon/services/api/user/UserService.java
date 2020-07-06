package com.project.drivemodeon.services.api.user;

import com.project.drivemodeon.model.service.users.UserSignInDto;
import com.project.drivemodeon.model.service.users.UserSignUpDto;
import com.project.drivemodeon.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    void signUpUser(UserSignUpDto userSignUpDto) throws Exception;

    UserSignInDto signInUser(UserSignInDto userSignInDto);

    boolean isEmailTaken(String email);

    boolean isUsernameTaken(String username);

    Optional<User> getUserById(long id);

    User getUserByUsername(String username);

    boolean isCurrentUserFollowProfileUser(User currentUser, User profileUser);

    void followUser(User loggedUser, User followingUser);

    void unfollowUser(User loggedUser, User followingUser);

    long getUserFollowersCount(User user);

    long getUserFollowingsCount(User user);

    void editUser(String username, Long userId);

    void addPost();
}
