package com.project.drivemodeon.service.impl.user;

import com.project.drivemodeon.exception.user.InvalidUserSignUp;
import com.project.drivemodeon.exception.user.signup.BaseSignUpException;
import com.project.drivemodeon.exception.user.signup.EmailAlreadyTaken;
import com.project.drivemodeon.exception.user.signup.PasswordsNotMatch;
import com.project.drivemodeon.exception.user.signup.UsernameAlreadyTaken;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.model.service.user.UserSignInDto;
import com.project.drivemodeon.repository.UserRepository;
import com.project.drivemodeon.service.api.role.RoleService;
import com.project.drivemodeon.service.api.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;

        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public void signUpUser(UserServiceModel userServiceModel) throws BaseSignUpException {
        this.roleService.seedRoles();

        if(this.userRepository.count() == 0){
            userServiceModel.setAuthorities(this.roleService.);
        }

        if (userServiceModel == null) {
            throw new InvalidUserSignUp();
        }

        String password = userServiceModel.getPassword();
        String confirmedPassword = userServiceModel.getConfirmPassword();

        if (!password.trim().equals(confirmedPassword.trim())) {
            throw new PasswordsNotMatch();
        }

        if (isEmailTaken(userServiceModel.getEmail())) {
            throw new EmailAlreadyTaken();
        }

        if (isUsernameTaken(userServiceModel.getUsername())) {
            throw new UsernameAlreadyTaken();
        }

        User user = modelMapper.map(userServiceModel, User.class);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.saveAndFlush(user);
    }

    @Override
    public UserSignInDto signInUser(UserServiceModel userServiceModel) {
        String username = userServiceModel.getUsername();

        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isEmpty()) {
            return null;

        }
        if (!passwordEncoder.matches(userServiceModel.getPassword(), user.get().getPassword())) {
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
    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            return null;
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
