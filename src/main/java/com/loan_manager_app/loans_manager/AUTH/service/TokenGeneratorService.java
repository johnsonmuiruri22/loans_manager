package com.loan_manager_app.loans_manager.AUTH.service;


import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class TokenGeneratorService {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String generateRefreshToken(){
        byte[] randomBytes = new byte[64];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }
}
