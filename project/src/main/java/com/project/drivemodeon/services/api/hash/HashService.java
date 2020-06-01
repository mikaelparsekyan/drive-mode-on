package com.project.drivemodeon.services.api.hash;

import org.springframework.stereotype.Service;

@Service
public interface HashService {
    String hash(String text);
}
