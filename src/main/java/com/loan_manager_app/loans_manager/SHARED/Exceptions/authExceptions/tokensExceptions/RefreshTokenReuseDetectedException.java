package com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions;

public class RefreshTokenReuseDetectedException extends RefreshTokenException{
    public RefreshTokenReuseDetectedException(String message) {
        super("Refresh token reuse detected");
    }
}
