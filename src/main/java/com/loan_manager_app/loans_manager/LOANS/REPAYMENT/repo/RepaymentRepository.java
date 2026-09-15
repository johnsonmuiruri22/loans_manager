package com.loan_manager_app.loans_manager.LOANS.REPAYMENT.repo;


import com.loan_manager_app.loans_manager.LOANS.REPAYMENT.modal.LoanRepayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface RepaymentRepository extends JpaRepository<LoanRepayment, Long> {
    Page<LoanRepayment> findAll(Pageable pageable);
    Page<LoanRepayment> findAllByCustomerId(Long customerId, Pageable pageable);

    @Query("""
        SELECT COALESCE(SUM(r.amountPaid), 0)
        FROM LoanRepayment r
        """)
    BigDecimal getTotalAmountPaid();

    @Query("""
        SELECT COALESCE(SUM(r.remainingBalance), 0)
        FROM LoanRepayment r
        WHERE r.id IN (
            SELECT MAX(r2.id)
            FROM LoanRepayment r2
            GROUP BY r2.loanId
        )
        """)
    BigDecimal getOutstandingBalance();
}
