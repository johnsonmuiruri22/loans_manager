package com.loan_manager_app.loans_manager.CUSTOMERS.modals;


import com.loan_manager_app.loans_manager.SHARED.ENUMS.CreditStatus;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.Gender;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.LoanStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate
@DynamicInsert
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String surname;
    private String otherNames;

    @Column(unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate dob;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(unique = true)
    private String email;
    private String residence;
    private String occupation;
    private BigDecimal monthlyIncome;

    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;

    @Builder.Default
    private boolean hasRequestedLoan = false;

    @Builder.Default
    private boolean hasRepaidLoan = false;
    private int loanCount = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public String getFullName(){
        return getSurname() + " " + getOtherNames();
    }

}
