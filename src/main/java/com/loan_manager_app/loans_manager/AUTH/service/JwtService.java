package com.loan_manager_app.loans_manager.AUTH.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;
import java.util.function.Function;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails, UUID sessionId);
    String extractUsername(String token);
    String generateRefreshToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    UUID extractSessionId(String token);
}
