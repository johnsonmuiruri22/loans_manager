package com.loan_manager_app.loans_manager.CUSTOMERS.Mapper;


import com.loan_manager_app.loans_manager.CUSTOMERS.customer_api.CustomerRespDTO;
import com.loan_manager_app.loans_manager.CUSTOMERS.modals.Customer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class CustomerMapper {
    public static CustomerRespDTO mapToCustomerRespDTO(Customer customer){
        return CustomerRespDTO.builder()
                .customerId(customer.getCustomerId())
                .surname(customer.getSurname())
                .otherNames(customer.getOtherNames())
                .email(customer.getEmail())
                .monthlyIncome(customer.getMonthlyIncome())
                .residence(customer.getResidence())
                .creditStatus(customer.getCreditStatus().toString())
                .monthlyIncome(customer.getMonthlyIncome())
                .dob(customer.getDob())
                .gender(customer.getGender())
                .age(calculateAge(customer.getDob()))
                .occupation(customer.getOccupation())
                .phone(customer.getPhone())
                .loanCount(customer.getLoanCount())
                .build();
    }

    private static int calculateAge(LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dob, currentDate).getYears();
    }
}
