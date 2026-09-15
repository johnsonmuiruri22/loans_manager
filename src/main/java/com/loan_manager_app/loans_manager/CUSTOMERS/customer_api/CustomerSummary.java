package com.loan_manager_app.loans_manager.CUSTOMERS.customer_api;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerSummary {
    private String customerFullName;
    private String phoneNumber;
    private String email;
    private String creditStatus;
    private BigDecimal monthlyIncome;
    private int loanCount;
    private String address;
}
