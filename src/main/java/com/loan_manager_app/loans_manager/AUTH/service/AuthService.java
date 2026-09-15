package com.loan_manager_app.loans_manager.AUTH.service;


import com.loan_manager_app.loans_manager.AUTH.dto.LoginRequest;
import com.loan_manager_app.loans_manager.AUTH.dto.LoginResponse;
import com.loan_manager_app.loans_manager.AUTH.dto.RegRequest;
import com.loan_manager_app.loans_manager.AUTH.events.CurrentUserResponse;
import com.loan_manager_app.loans_manager.SHARED.appResponse.AppResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest,
            HttpServletRequest request
            ,HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
    ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response);
    AppResponse RegisterUser(RegRequest regRequest, HttpServletRequest request);
    CurrentUserResponse currentUser();
}
