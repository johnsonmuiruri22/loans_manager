package com.loan_manager_app.loans_manager.LOANS.controller;


import com.loan_manager_app.loans_manager.LOANS.DTOS.LoanProcessedResponse;
import com.loan_manager_app.loans_manager.LOANS.DTOS.LoanProcessingInfo;
import com.loan_manager_app.loans_manager.LOANS.DTOS.LoanRequests;
import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.dto.PaymentRecordDTO;
import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.dto.RepaymentRequest;
import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.service.LoanRepaymentService;
import com.loan_manager_app.loans_manager.LOANS.service.LoanService;
import com.loan_manager_app.loans_manager.SHARED.appResponse.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;
    private final LoanRepaymentService loanRepaymentService;


    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @GetMapping("/get/all")
    public ResponseEntity<PageResponse<LoanRequests>> getLoanRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(loanService.getAllLoans(page, size));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @GetMapping("/get/loan/{loanId}")
    public ResponseEntity<LoanRequests> getLoanRecord(@PathVariable Long loanId){
        return ResponseEntity.ok(loanService.getLoanById(loanId));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @PutMapping("/process/loan")
    public ResponseEntity<LoanProcessedResponse> processLoan(
            @RequestBody LoanProcessingInfo loanProcessingInfo){
        return ResponseEntity.ok(loanService.processLoan(loanProcessingInfo));
    }


    // loan repayment

    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @GetMapping("/repayments")
    public ResponseEntity<PageResponse<PaymentRecordDTO>> getLoanRepayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(loanRepaymentService.getPaymentRecords(page, size));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @PutMapping("/repayments/pay")
    public ResponseEntity<PaymentRecordDTO> pay(@RequestBody RepaymentRequest request){
        return ResponseEntity.ok(loanRepaymentService.pay(request));
    }
}

