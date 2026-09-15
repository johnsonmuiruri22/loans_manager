package com.loan_manager_app.loans_manager.AI.modal;


import com.loan_manager_app.loans_manager.AI.dto.LoanAiAssessmentResponse;
import com.loan_manager_app.loans_manager.AI.enums.AiLoanRecommendation;
import com.loan_manager_app.loans_manager.AI.enums.AiLoanRiskLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LoanAiAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long loanId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AiLoanRecommendation recommendation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AiLoanRiskLevel riskLevel;

    @Column(columnDefinition = "TEXT")
    private String reasoning;

    @Column(precision = 19, scale = 2)
    private BigDecimal suggestedMaximumAmount;

    private double confidence;

    @Column(nullable = false)
    private LocalDateTime assessedAt;
}
