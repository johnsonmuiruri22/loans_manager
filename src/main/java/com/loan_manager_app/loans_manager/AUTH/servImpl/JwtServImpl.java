package com.loan_manager_app.loans_manager.AUTH.servImpl;


import com.loan_manager_app.loans_manager.AUTH.security.JwtProperties;
import com.loan_manager_app.loans_manager.AUTH.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServImpl implements JwtService {
    private final JwtProperties jwtProperties;

    @Override
    public String generateAccessToken(UserDetails userDetails, UUID sessionId) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roles);
        claims.put("sid", sessionId);
        return buildToken(
                claims,
                userDetails,
                jwtProperties.getAccessTokenExpiration()
        );
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(
                Collections.emptyMap(),
                userDetails,
                jwtProperties.getRefreshTokenExpiration()
        );
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public UUID extractSessionId(String token) {
        return null;
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Date extractExpirationDate(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private SecretKey getSigningKey(){
        byte[]keyBytes = Base64.getDecoder().decode(jwtProperties.getSecretKey());
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    private String buildToken(Map<String, Object> claims,
                              UserDetails userDetails,
                              long expiration){
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }
}
