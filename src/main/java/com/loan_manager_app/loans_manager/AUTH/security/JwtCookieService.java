package com.loan_manager_app.loans_manager.AUTH.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtCookieService {

    private final JwtProperties jwtProperties;

    private static final String ACCESS_COOKIE = "accessToken";
    private static final String REFRESH_COOKIE = "refreshToken";

    private long accessCookieAge() {
        return jwtProperties.getAccessTokenExpiration() / 1000;
    }

    private long refreshCookieAge() {
        return jwtProperties.getRefreshTokenExpiration() / 1000;
    }


    private ResponseCookie buildCookie(
            String cookieName,
            String token,
            long maxAge
    ) {

        return ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(jwtProperties.getCookie().isSecure())
                .path(jwtProperties.getCookie().getPath())
                .sameSite(jwtProperties.getCookie().getSameSite())
                .maxAge(maxAge)
                .build();
    }

    private ResponseCookie deleteCookie(String cookieName) {

        return ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(jwtProperties.getCookie().isSecure())
                .path(jwtProperties.getCookie().getPath())
                .sameSite(jwtProperties.getCookie().getSameSite())
                .maxAge(0)
                .build();
    }

    public void addAccessTokenCookie(
            HttpServletResponse response,
            String token
    ) {

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                buildCookie(
                        ACCESS_COOKIE,
                        token,
                        accessCookieAge()
                ).toString()
        );
    }

    public void addRefreshTokenCookie(
            HttpServletResponse response,
            String token
    ) {

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                buildCookie(
                        REFRESH_COOKIE,
                        token,
                        refreshCookieAge()
                ).toString()
        );
    }

    public void clearAccessTokenCookie(HttpServletResponse response) {

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                deleteCookie(ACCESS_COOKIE).toString()
        );
    }

    public void clearRefreshTokenCookie(HttpServletResponse response) {

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                deleteCookie(REFRESH_COOKIE).toString()
        );
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {

        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> ACCESS_COOKIE.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {

        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> REFRESH_COOKIE.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
