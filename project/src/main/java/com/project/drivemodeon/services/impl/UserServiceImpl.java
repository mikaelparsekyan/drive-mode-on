package com.project.drivemodeon.services.impl;

import com.project.drivemodeon.domain.dtos.users.UserEditDto;
import com.project.drivemodeon.domain.dtos.users.UserExposeInfoDto;
import com.project.drivemodeon.domain.dtos.users.UserSignInDto;
import com.project.drivemodeon.domain.dtos.users.UserSignUpDto;
import com.project.drivemodeon.domain.models.BaseEntity;
import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.repositories.UserRepository;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.validation.constants.ValidationConstants;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean signUpUser(UserSignUpDto userSignUpDto) {
        String password = userSignUpDto.getPassword();
        String confirmedPassword = userSignUpDto.getConfirmPassword();

        if (password.trim().equals(confirmedPassword.trim())) {
            User user = modelMapper.map(userSignUpDto, User.class);

            String hashPassword = DigestUtils.sha256Hex(user.getPassword());
            user.setPassword(hashPassword);

            userRepository.saveAndFlush(user);

            return true;
        }
        return false;
    }

    @Override
    public long signInUser(UserSignInDto userSignInDto) {
        String username = userSignInDto.getUsername();

        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isEmpty()) {
            return -1;

        }
        String passwordHash = DigestUtils.sha256Hex(userSignInDto.getPassword());

        if (!(user.get().getPassword().equals(passwordHash))) {
            return -1;
        }

        return user.get().getId();
    }

    @Override
    public boolean isEmailTaken(String email) {
        Optional<User> foundUser = userRepository.findUserByEmail(email);
        return foundUser.isPresent();
    }

    @Override
    public boolean isUsernameTaken(String username) {
        Optional<User> foundUser = userRepository.findUserByUsername(username);
        return foundUser.isPresent();
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public boolean isCurrentUserFollowProfileUser(User currentUser, User profileUser) {
        return currentUser.getFollowing().contains(profileUser);
    }

    @Override
    public void followUser(User loggedUser, User followingUser) {
        loggedUser.getFollowing().add(followingUser);
    }

    @Override
    public void unfollowUser(User loggedUser, User followingUser) {
        loggedUser.getFollowing().remove(followingUser);
    }

    @Override
    public long getUserFollowersCount(User user) {
        return (long) userRepository.getAllFollowersByUsername(user.getUsername()).size();
    }

    @Override
    public long getUserFollowingsCount(User user) {
        return (long) userRepository.getAllFollowingsByUsername(user.getUsername()).size();
    }

    @Transactional
    @Override
    public void editUser(String username, Long userId) {
        userRepository.updateUserById(username, userId);
    }
}
