package com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions;

public class RefreshTokenExpiredException extends RefreshTokenException{
    public RefreshTokenExpiredException(String message) {
        super("Refresh token expired");
    }
}
