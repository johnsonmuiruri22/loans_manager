package com.loan_manager_app.loans_manager.LOANS.REPAYMENT.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepaymentRequest {
    private Long repaymentId;
    private BigDecimal amountToPay;
}
