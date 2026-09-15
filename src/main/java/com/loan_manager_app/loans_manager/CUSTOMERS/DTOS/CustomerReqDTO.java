package com.loan_manager_app.loans_manager.CUSTOMERS.DTOS;

import com.loan_manager_app.loans_manager.SHARED.ENUMS.Gender;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerReqDTO {
    private String surname;
    private String otherNames;
    private Gender gender;
    private LocalDate dob;
    private String phone;
    private String email;
    private String residence;
    private String occupation;
    private BigDecimal monthlyIncome;
}
