package com.project.drivemodeon.services.impl.hash;

import com.project.drivemodeon.services.api.hash.BCryptService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptServiceImpl implements BCryptService {
    private final PasswordEncoder bCryptPasswordEncoder;

    public BCryptServiceImpl(PasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public String crypt(String text) {
        return bCryptPasswordEncoder.encode(text);
    }
}
