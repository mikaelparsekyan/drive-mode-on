package com.project.drivemodeon.service.api.user;

import com.project.drivemodeon.exception.user.signup.BaseSignUpException;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public interface UserService {
    void signUpUser(UserServiceModel userServiceModel) throws BaseSignUpException;

    boolean signInUser(UserServiceModel userServiceModel);

    boolean isEmailTaken(String email);

    boolean isUsernameTaken(String username);

    Optional<User> getUserById(long id);

    User getUserByUsername(String username);

    boolean isCurrentUserFollowProfileUser(User currentUser, User profileUser);

    void followUser(User loggedUser, User followingUser);

    void unfollowUser(User loggedUser, User followingUser);

    long getUserFollowersCount(User user);

    long getUserFollowingsCount(User user);

    void editUser(UserServiceModel userServiceModel);

    void uploadProfileImage(MultipartFile image, User user);
}
