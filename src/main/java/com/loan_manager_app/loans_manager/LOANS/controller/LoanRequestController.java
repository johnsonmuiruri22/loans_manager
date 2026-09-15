package com.loan_manager_app.loans_manager.LOANS.controller;

import com.loan_manager_app.loans_manager.LOANS.DTOS.CustomerLoanRequest;
import com.loan_manager_app.loans_manager.LOANS.DTOS.LoanRequestDTO;
import com.loan_manager_app.loans_manager.LOANS.service.LoanRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class LoanRequestController {

    private final LoanRequestService loanRequestService;

    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @PostMapping("/requestLoan")
    public ResponseEntity<CustomerLoanRequest> loanRequest(
            @RequestBody LoanRequestDTO loanRequestDTO) {

        return ResponseEntity.ok(
                loanRequestService.requestLoan(loanRequestDTO)
        );
    }
}
