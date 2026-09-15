package com.loan_manager_app.loans_manager.SHARED.appResponse;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerRespDTO;
import com.loan_manager_app.loans_manager.LOANS.loan_api.LoanDetailsDTO;
import com.loan_manager_app.loans_manager.SHARED.sharedMessages.AuthResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppResponse {
    private String message;
    private int statusCode;
    private AuthResponse authResponse;

    //customers
    private CustomerRespDTO customerToGet;
    private List<CustomerRespDTO> customers;

    //loans
    private LoanDetailsDTO loanToGet;
    private List<LoanDetailsDTO> loans;
}
