package com.loan_manager_app.loans_manager.LOANS.DTOS;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class LoanRequestDTO {
    private Long customerId;
    private BigDecimal loanAmount;
    private String loanPurpose;
    private int duration; //months
    private LocalDateTime requestedAt;
}
