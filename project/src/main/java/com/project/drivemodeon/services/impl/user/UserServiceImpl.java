package com.project.drivemodeon.services.impl.user;

import com.project.drivemodeon.exception.user.InvalidUserSignUp;
import com.project.drivemodeon.exception.user.UserNotExistException;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.users.UserSignInDto;
import com.project.drivemodeon.model.service.users.UserSignUpDto;
import com.project.drivemodeon.repositories.UserRepository;
import com.project.drivemodeon.services.api.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;

        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void signUpUser(UserSignUpDto userSignUpDto) throws Exception {
        if (userSignUpDto == null) {
            throw new InvalidUserSignUp();
        }

        String password = userSignUpDto.getPassword();
        String confirmedPassword = userSignUpDto.getConfirmPassword();

        if (!password.trim().equals(confirmedPassword.trim())) {
            throw new InvalidUserSignUp();
        }

        User user = modelMapper.map(userSignUpDto, User.class);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.saveAndFlush(user);
    }

    @Override
    public UserSignInDto signInUser(UserSignInDto userSignInDto) {
        String username = userSignInDto.getUsername();

        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isEmpty()) {
            return null;

        }
        if (!passwordEncoder.matches(userSignInDto.getPassword(), user.get().getPassword())) {
            return null;
        }

        return modelMapper.map(user.get(), UserSignInDto.class);
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
    public User getUserByUsername(String username) throws Exception {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotExistException();
        }
        return user.get();
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

    @Override
    public void addPost() {
        //TODO
    }
}
