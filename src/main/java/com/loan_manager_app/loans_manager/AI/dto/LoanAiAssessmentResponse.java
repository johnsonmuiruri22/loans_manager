package com.loan_manager_app.loans_manager.AI.dto;


import com.loan_manager_app.loans_manager.AI.enums.AiLoanRecommendation;
import com.loan_manager_app.loans_manager.AI.enums.AiLoanRiskLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanAiAssessmentResponse {
    private AiLoanRecommendation recommendation;
    private AiLoanRiskLevel riskLevel;
    private String reasoning;
    private BigDecimal suggestedMaximumAmount;
    private double confidence;
}
