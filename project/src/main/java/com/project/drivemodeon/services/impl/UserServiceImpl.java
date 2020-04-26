package com.project.drivemodeon.services.impl;

import com.project.drivemodeon.domain.dtos.users.UserSignUpDto;
import com.project.drivemodeon.domain.models.User;
import com.project.drivemodeon.repositories.UserRepository;
import com.project.drivemodeon.services.api.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
