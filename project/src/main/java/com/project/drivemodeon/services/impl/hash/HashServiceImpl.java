package com.project.drivemodeon.services.impl.hash;

import com.project.drivemodeon.services.api.hash.HashService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class HashServiceImpl implements HashService {
    @Override
    public String hash(String text) {
        return DigestUtils.sha256Hex(text);
    }
}
