package com.project.drivemodeon.service.impl.user;

import com.project.drivemodeon.exception.user.InvalidUserSignUp;
import com.project.drivemodeon.exception.user.signup.BaseSignUpException;
import com.project.drivemodeon.exception.user.signup.EmailAlreadyTaken;
import com.project.drivemodeon.exception.user.signup.PasswordsNotMatch;
import com.project.drivemodeon.exception.user.signup.UsernameAlreadyTaken;
import com.project.drivemodeon.model.entity.AuthorityEntity;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.entity.UserBio;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.repository.UserRepository;
import com.project.drivemodeon.service.api.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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
    public void signUpUser(UserServiceModel userServiceModel) throws BaseSignUpException {
        if (userServiceModel == null) {
            throw new InvalidUserSignUp();
        }
        User userEntity = modelMapper.map(userServiceModel, User.class);

        AuthorityEntity authorityEntity = new AuthorityEntity();
        if (this.userRepository.count() == 0) {
            authorityEntity.setUser(userEntity);
            authorityEntity.setName("ROLE_ADMIN");
        } else {
            authorityEntity.setUser(userEntity);
            authorityEntity.setName("ROLE_USER");
        }
        userEntity.setAuthorities(List.of(authorityEntity));

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

        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);

        userRepository.saveAndFlush(userEntity);
    }

    @Override
    public boolean signInUser(UserServiceModel userServiceModel) {
        String username = userServiceModel.getUsername();

        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isEmpty()) {
            return false;

        }
        if (!passwordEncoder.matches(userServiceModel.getPassword(), user.get().getPassword())) {
            return false;
        }
        return true;
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
    public void editUser(UserServiceModel userServiceModel) {
        if (userServiceModel != null) {
            Optional<User> user = getUserById(userServiceModel.getId());
            if (user.isPresent()) {
                user.get().setFirstName(userServiceModel.getFirstName());
                user.get().setLastName(userServiceModel.getLastName());
                user.get().setUsername(userServiceModel.getUsername());
                user.get().setEmail(userServiceModel.getEmail());
                user.get().setPassword(passwordEncoder.encode(userServiceModel.getPassword()));
                if (user.get().getBio() != null) {
                    UserBio userBio = user.get().getBio();
                    userBio.setValue(userServiceModel.getBio());
                    user.get().setBio(userBio);
                } else {
                    user.get().setBio(new UserBio(userServiceModel.getBio()));
                }
                userRepository.saveAndFlush(user.get());
            }
        }
    }

    @Override
    public void addPost() {
        //TODO
    }
}
