package com.loan_manager_app.loans_manager.LOANS.REPAYMENT.dto;


import com.loan_manager_app.loans_manager.SHARED.ENUMS.RepaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PaymentRecordDTO {
    private String message;
    private Long repaymentId;
    private String customerFullName;
    private BigDecimal amountPaid;
    private BigDecimal totalAmountToBeRepaid;
    private BigDecimal remainingBalance;
    private LocalDateTime repaidAt;
    private String repaymentStatus;
}
