package com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions;

import com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.AuthenticationException;

public abstract class RefreshTokenException extends AuthenticationException {

    public RefreshTokenException(String message) {
        super(message);
    }
}
