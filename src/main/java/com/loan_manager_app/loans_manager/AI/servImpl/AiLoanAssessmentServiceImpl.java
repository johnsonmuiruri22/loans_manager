package com.loan_manager_app.loans_manager.AI.servImpl;

import com.loan_manager_app.loans_manager.AI.dto.LoanAiAssessmentRequest;
import com.loan_manager_app.loans_manager.AI.dto.LoanAiAssessmentResponse;
import com.loan_manager_app.loans_manager.AI.enums.AiLoanRecommendation;
import com.loan_manager_app.loans_manager.AI.enums.AiLoanRiskLevel;
import com.loan_manager_app.loans_manager.AI.mapper.LoanAiAssessmentMapper;
import com.loan_manager_app.loans_manager.AI.modal.LoanAiAssessment;
import com.loan_manager_app.loans_manager.AI.repo.LoanAiAssessmentRepository;
import com.loan_manager_app.loans_manager.AI.service.AiLoanAssessmentService;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerLookupService;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerSummary;
import com.loan_manager_app.loans_manager.LOANS.loan_api.LoanLookupService;
import com.loan_manager_app.loans_manager.LOANS.loan_api.LoanSummary;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.StructuredResponse;
import com.openai.models.responses.StructuredResponseCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiLoanAssessmentServiceImpl
        implements AiLoanAssessmentService {

    private final OpenAIClient openAIClient;
    private final LoanAiAssessmentRepository loanAiAssessmentRepository;
    private final CustomerLookupService customerLookupService;
    private final LoanLookupService loanLookupService;
    private final LoanAiAssessmentMapper loanAiAssessmentMapper;

    @Override
    public LoanAiAssessmentResponse assessLoan(
            LoanAiAssessmentRequest request) {

        String prompt = """
            You are an AI loan assessment assistant.

            Analyze the following loan applicant.

            Monthly income: KES %s
            Credit status: %s
            Number of previous loans: %d
            Requested loan amount: KES %s

            Assess the applicant ONLY using the information provided.

            Consider:
            1. Requested loan amount relative to monthly income.
            2. Credit status.
            3. Number of previous loans.

            Recommendation must be one of:
            APPROVE, REVIEW, REJECT.

            Risk level must be one of:
            LOW, MEDIUM, HIGH.
          
            The reasoning must be consistent with the selected recommendation
            and risk level.

            Suggested maximum loan amount must be numeric.

            Confidence must be between 0 and 1.

            Provide a concise reasoning for your assessment.

            Do not invent information that was not provided.
            """.formatted(
                request.getMonthlyIncome(),
                request.getCreditStatus(),
                request.getLoanCount(),
                request.getLoanAmountRequested()
        );

        StructuredResponseCreateParams<LoanAiAssessmentResponse> params =
                ResponseCreateParams.builder()
                        .model(ChatModel.GPT_5_2)
                        .input(prompt)
                        .text(LoanAiAssessmentResponse.class)
                        .build();

        StructuredResponse<LoanAiAssessmentResponse> response =
                openAIClient.responses().create(params);

        return response.output()
                .stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No structured AI response received"
                        ));
    }

    @Override
    public LoanAiAssessmentResponse assessLoan(Long loanId) {

        Optional<LoanAiAssessment> existingAssessment =
                loanAiAssessmentRepository.findByLoanId(loanId);

        if (existingAssessment.isPresent()) {

            return mapToResponse(
                    existingAssessment.get()
            );
        }

        LoanSummary loanInfo =
                loanLookupService.getLoanSummary(loanId);

        CustomerSummary customer =
                customerLookupService.getCustomerSummary(loanInfo.customerId());


        if (customer.getMonthlyIncome() == null) {
            throw new IllegalStateException(
                    "Customer monthly income is missing"
            );
        }

        if (customer.getCreditStatus() == null) {
            throw new IllegalStateException(
                    "Customer credit status is missing"
            );
        }

        if (loanInfo.loanAmountRequested() == null) {
            throw new IllegalStateException(
                    "Loan requested amount is missing"
            );
        }

        LoanAiAssessmentRequest request =
                LoanAiAssessmentRequest.builder()
                        .monthlyIncome(
                                customer.getMonthlyIncome()
                        )
                        .creditStatus(
                                customer.getCreditStatus()
                        )
                        .loanCount(
                                customer.getLoanCount()
                        )
                        .loanAmountRequested(
                                loanInfo.loanAmountRequested()
                        )
                        .build();

        LoanAiAssessmentResponse response =
                assessLoan(request);

        validateAiResponse(response);

        saveAssessment(
                loanId,
                response
        );

        return response;
    }

    @Override
    public LoanAiAssessmentResponse getAssessment(Long loanId) {
        LoanAiAssessment assessment =
                loanAiAssessmentRepository.findByLoanId(loanId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No AI assessment found for loan: " + loanId
                                )
                        );

        return LoanAiAssessmentResponse.builder()
                .recommendation(assessment.getRecommendation())
                .riskLevel(assessment.getRiskLevel())
                .reasoning(assessment.getReasoning())
                .suggestedMaximumAmount(
                        assessment.getSuggestedMaximumAmount()
                )
                .confidence(assessment.getConfidence())
                .build();
    }



    private void saveAssessment(
            Long loanId,
            LoanAiAssessmentResponse response) {

        LoanAiAssessment assessment =
                LoanAiAssessment.builder()
                        .loanId(loanId)
                        .recommendation(
                                AiLoanRecommendation.valueOf(
                                        response.getRecommendation().name()
                                )
                        )
                        .riskLevel(
                                AiLoanRiskLevel.valueOf(
                                        response.getRiskLevel().name()
                                )
                        )
                        .reasoning(
                                response.getReasoning()
                        )
                        .suggestedMaximumAmount(
                                response.getSuggestedMaximumAmount()
                        )
                        .confidence(
                                response.getConfidence()
                        )
                        .assessedAt(
                                LocalDateTime.now()
                        )
                        .build();

        loanAiAssessmentRepository.save(assessment);
    }

    private LoanAiAssessmentResponse mapToResponse(
            LoanAiAssessment assessment) {
        return new LoanAiAssessmentResponse(
                assessment.getRecommendation(),
                assessment.getRiskLevel(),
                assessment.getReasoning(),
                assessment.getSuggestedMaximumAmount(),
                assessment.getConfidence()
        );
    }

    private void validateAiResponse(
            LoanAiAssessmentResponse response) {
        if (response == null) {
            throw new IllegalStateException(
                    "AI assessment response is null"
            );
        }

        if (response.getRecommendation() == null) {
            throw new IllegalStateException(
                    "AI recommendation is missing"
            );
        }

        if (response.getRiskLevel() == null) {
            throw new IllegalStateException(
                    "AI risk level is missing"
            );
        }

        if (response.getReasoning() == null ||
                response.getReasoning().isBlank()) {

            throw new IllegalStateException(
                    "AI reasoning is missing"
            );
        }

        if (response.getSuggestedMaximumAmount() == null ||
                response.getSuggestedMaximumAmount().compareTo(
                        BigDecimal.ZERO
                ) < 0) {

            throw new IllegalStateException(
                    "AI suggested maximum amount is invalid"
            );
        }

        if (response.getConfidence() < 0 ||
                response.getConfidence() > 1) {

            throw new IllegalStateException(
                    "AI confidence must be between 0 and 1"
            );
        }
    }
}