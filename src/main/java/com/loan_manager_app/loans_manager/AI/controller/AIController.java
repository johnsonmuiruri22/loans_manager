package com.loan_manager_app.loans_manager.AI.controller;


import com.loan_manager_app.loans_manager.AI.dto.LoanAiAssessmentRequest;
import com.loan_manager_app.loans_manager.AI.dto.LoanAiAssessmentResponse;
import com.loan_manager_app.loans_manager.AI.service.AiLoanAssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {
    private final AiLoanAssessmentService aiLoanAssessmentService;



    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @PostMapping("/loan-assessment/{loanId}")
    public ResponseEntity<LoanAiAssessmentResponse> assessLoan(@PathVariable Long loanId) {

        return ResponseEntity.ok(
                aiLoanAssessmentService.assessLoan(loanId)
        );
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'BANKER')")
    @GetMapping("/loan-assessment/{loanId}")
    public ResponseEntity<LoanAiAssessmentResponse> getAssessment(
            @PathVariable Long loanId) {
        return ResponseEntity.ok(
                aiLoanAssessmentService.getAssessment(loanId)
        );
    }

}
