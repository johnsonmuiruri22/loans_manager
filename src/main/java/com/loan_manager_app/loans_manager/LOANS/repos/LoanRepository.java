package com.loan_manager_app.loans_manager.LOANS.repos;


import com.loan_manager_app.loans_manager.LOANS.modals.Loan;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Page<Loan> findAll(Pageable pageable);

    long countByLoanStatus(LoanStatus loanStatus);

    @Query("""
        SELECT COALESCE(SUM(l.loanAmountBorrowed), 0)
        FROM Loan l
        """)
    BigDecimal getTotalAmountBorrowed();


    @Query("""
        SELECT COALESCE(SUM(l.loanAmountIssued), 0)
        FROM Loan l
        """)
    BigDecimal getTotalAmountIssued();


    @Query("""
    SELECT
        FUNCTION('TO_CHAR', l.issuedAt, 'YYYY-MM'),
        COALESCE(SUM(l.loanAmountBorrowed), 0)
    FROM Loan l
    WHERE l.issuedAt >= :start
      AND l.issuedAt < :end
    GROUP BY FUNCTION('TO_CHAR', l.issuedAt, 'YYYY-MM')
    ORDER BY FUNCTION('TO_CHAR', l.issuedAt, 'YYYY-MM')
    """)
    List<Object[]> getBorrowingTrend(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
    SELECT
        FUNCTION('TO_CHAR', l.issuedAt, 'YYYY-MM-DD'),
        COALESCE(SUM(l.loanAmountBorrowed), 0)
    FROM Loan l
    WHERE l.issuedAt >= :start
      AND l.issuedAt < :end
    GROUP BY FUNCTION('TO_CHAR', l.issuedAt, 'YYYY-MM-DD')
    ORDER BY FUNCTION('TO_CHAR', l.issuedAt, 'YYYY-MM-DD')
    """)
    List<Object[]> getDailyBorrowingTrend(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
