package com.loan_manager_app.loans_manager.AUTH.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan_manager_app.loans_manager.SHARED.appResponse.ApiErrorResponse;
import com.loan_manager_app.loans_manager.SHARED.appResponse.AppResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomDenialHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    @Override
    public void handle(@NonNull HttpServletRequest request,
                       @NonNull HttpServletResponse response,
                       @NonNull AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .error("Access Denied")
                .message(accessDeniedException.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .statusCode(HttpServletResponse.SC_FORBIDDEN)
                .build();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
