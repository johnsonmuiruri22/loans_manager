package com.loan_manager_app.loans_manager.CUSTOMERS.customer_api;


import com.loan_manager_app.loans_manager.SHARED.ENUMS.Gender;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerRespDTO {
    private Long customerId;
    private String surname;
    private String otherNames;
    private Gender gender;
    private LocalDate dob;
    private int age;
    private String phone;
    private String email;
    private String residence;
    private String occupation;
    private BigDecimal monthlyIncome;
    private int loanCount;
    private String creditStatus;
    private String customMessage;
}
