package com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions;

public class RefreshTokenRevokedException extends RefreshTokenException{
    public RefreshTokenRevokedException(String message) {
        super("Refresh token has been revoked");
    }
}
