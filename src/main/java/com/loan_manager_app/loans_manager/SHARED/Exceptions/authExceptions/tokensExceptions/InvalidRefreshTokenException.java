package com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions;

public class InvalidRefreshTokenException extends RefreshTokenException{
    public InvalidRefreshTokenException(String message) {
        super("Invalid refresh token");
    }
}
