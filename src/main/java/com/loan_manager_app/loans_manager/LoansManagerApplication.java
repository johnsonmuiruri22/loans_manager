package com.loan_manager_app.loans_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.loan_manager_app.loans_manager")
public class LoansManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoansManagerApplication.class, args);
    }

}
