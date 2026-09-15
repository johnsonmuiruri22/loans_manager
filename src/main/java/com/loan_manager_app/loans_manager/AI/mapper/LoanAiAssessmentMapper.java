package com.loan_manager_app.loans_manager.AI.mapper;


import com.loan_manager_app.loans_manager.AI.dto.LoanAiAssessmentResponse;
import com.loan_manager_app.loans_manager.AI.modal.LoanAiAssessment;
import org.springframework.stereotype.Component;

@Component
public class LoanAiAssessmentMapper {

    public LoanAiAssessmentResponse toResponse(
            LoanAiAssessment assessment
    ){
        return new LoanAiAssessmentResponse(
                assessment.getRecommendation(),
                assessment.getRiskLevel(),
                assessment.getReasoning(),
                assessment.getSuggestedMaximumAmount(),
                assessment.getConfidence()
        );

    }
}
