package com.project.drivemodeon.services.api.hash;

import org.springframework.stereotype.Service;

@Service
public interface BCryptService {
    String crypt(String text);
}
