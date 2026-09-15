package com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions;

public class RefreshTokenRotatedException extends RefreshTokenException{
    public RefreshTokenRotatedException(String message) {
        super("Refresh token has already been rotated");
    }
}
