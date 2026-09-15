package com.loan_manager_app.loans_manager.AI.dto;


import lombok.*;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoanAiAssessmentRequest {
    private BigDecimal monthlyIncome;
    private String creditStatus;
    private int loanCount;
    private BigDecimal loanAmountRequested;
}
