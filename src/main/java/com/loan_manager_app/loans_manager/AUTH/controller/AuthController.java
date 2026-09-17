package com.loan_manager_app.loans_manager.AUTH.controller;


import com.loan_manager_app.loans_manager.AUTH.dto.LoginRequest;
import com.loan_manager_app.loans_manager.AUTH.dto.LoginResponse;
import com.loan_manager_app.loans_manager.AUTH.dto.RegRequest;
import com.loan_manager_app.loans_manager.AUTH.events.CurrentUserResponse;
import com.loan_manager_app.loans_manager.AUTH.mapper.CurrentUserMapper;
import com.loan_manager_app.loans_manager.AUTH.security.CustomUserDetails;
import com.loan_manager_app.loans_manager.AUTH.service.AuthService;
import com.loan_manager_app.loans_manager.SHARED.appResponse.AppResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response){
        return ResponseEntity.ok(authService.login(loginRequest, request, response));
    }

    @PostMapping("/register")
    public ResponseEntity<AppResponse> registerUser(@RequestBody RegRequest registerRequest,
                                                    HttpServletRequest request){
        return ResponseEntity.ok(authService.RegisterUser(registerRequest, request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.refreshToken(request, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<AppResponse> logout(HttpServletRequest request,
                                               HttpServletResponse response){
        authService.logout(request, response);
        return ResponseEntity.ok(
                AppResponse.builder()
                        .message("User logged out successfully")
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> currentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(
                CurrentUserMapper.map(userDetails.getUser())
        );
    }
}
