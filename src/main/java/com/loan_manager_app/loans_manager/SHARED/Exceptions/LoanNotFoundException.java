package com.loan_manager_app.loans_manager.SHARED.Exceptions;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(String message) {
        super(message);
    }
}
