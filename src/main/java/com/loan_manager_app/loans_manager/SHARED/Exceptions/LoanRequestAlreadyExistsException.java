package com.loan_manager_app.loans_manager.SHARED.Exceptions;

public class LoanRequestAlreadyExistsException extends RuntimeException {
    public LoanRequestAlreadyExistsException(String message) {
        super(message);
    }
}
