package com.project.drivemodeon.services.impl;

import com.project.drivemodeon.domain.dtos.users.UserSignInDto;
import com.project.drivemodeon.domain.dtos.users.UserSignUpDto;
import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.repositories.UserRepository;
import com.project.drivemodeon.services.api.UserService;
import com.project.drivemodeon.validation.constants.ValidationConstants;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public boolean signInUser(UserSignInDto userSignInDto) {
        String username = userSignInDto.getUsername();

        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isEmpty()) {
            return false;

        }
        String passwordHash = DigestUtils.sha256Hex(userSignInDto.getPassword());

        return user.get().getPassword().equals(passwordHash);
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

}
