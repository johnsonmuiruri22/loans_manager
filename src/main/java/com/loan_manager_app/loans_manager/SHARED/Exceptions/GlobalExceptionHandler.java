package com.loan_manager_app.loans_manager.SHARED.Exceptions;


import com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.AuthenticationException;
import com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions.*;
import com.loan_manager_app.loans_manager.SHARED.appResponse.ApiErrorResponse;
import com.loan_manager_app.loans_manager.SHARED.appResponse.AppResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleRuntimeException(RuntimeException ex,
                                                                   HttpServletRequest request) {
        ApiErrorResponse runtimeError = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error("Internal Server Error")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(runtimeError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleRoleNotFoundException(
            RoleNotFoundException ex,
            HttpServletRequest request) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .error("Role not found")
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex,
            HttpServletRequest request) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .error("User not found")
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomerNotFoundException(
            CustomerNotFoundException ex,
            HttpServletRequest request) {
        ApiErrorResponse errorResp = ApiErrorResponse.builder()
                .error("Customer not found")
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResp);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {
        ApiErrorResponse authErrorResp = ApiErrorResponse.builder()
                .error("Authentication Error")
                .message(ex.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authErrorResp);
    }

    @ExceptionHandler({
            InvalidRefreshTokenException.class,
            RefreshTokenExpiredException.class,
            RefreshTokenReuseDetectedException.class
    })
    public ResponseEntity<ApiErrorResponse> handleRefreshTokenExceptions(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .error("Authorization Error")
                .message(ex.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrorResponse);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleLoanNotFoundException(
            LoanNotFoundException ex,
            HttpServletRequest request
    ){
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .error("Loan not found")
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(LoanAmountExceedsBorrowedAmountException.class)
    public ResponseEntity<ApiErrorResponse> handleLoanAmountExceedsBorrowedAmountException(
            LoanAmountExceedsBorrowedAmountException ex,
            HttpServletRequest request
    ){
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .error("Loan Error")
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
