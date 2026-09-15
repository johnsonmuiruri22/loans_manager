package com.loan_manager_app.loans_manager.CUSTOMERS.repo;


import com.loan_manager_app.loans_manager.CUSTOMERS.modals.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmailAndPhone(String email, String phone);
    Page<Customer> findAll(Pageable pageable);
    Optional<Customer> findByCustomerId(Long customerId);


    boolean
    existsByCustomerIdAndHasRequestedLoanTrueAndHasRepaidLoanFalse(Long customerId);

    long countByCreatedAtAfter(LocalDateTime dateTime);
    List<Customer> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("""
        SELECT
            FUNCTION('TO_CHAR', c.createdAt, 'YYYY-MM'),
            COUNT(c.customerId)
        FROM Customer c
        WHERE c.createdAt >= :start
          AND c.createdAt < :end
        GROUP BY FUNCTION('TO_CHAR', c.createdAt, 'YYYY-MM')
        ORDER BY FUNCTION('TO_CHAR', c.createdAt, 'YYYY-MM')
        """)
    List<Object[]> getRegistrationTrend(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
    SELECT
        FUNCTION('TO_CHAR', c.createdAt, 'YYYY-MM-DD'),
        COUNT(c.customerId)
    FROM Customer c
    WHERE c.createdAt >= :start
      AND c.createdAt < :end
    GROUP BY FUNCTION('TO_CHAR', c.createdAt, 'YYYY-MM-DD')
    ORDER BY FUNCTION('TO_CHAR', c.createdAt, 'YYYY-MM-DD')
    """)
    List<Object[]> getDailyRegistrationTrend(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
