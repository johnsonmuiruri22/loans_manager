package com.loan_manager_app.loans_manager.AI.repo;


import com.loan_manager_app.loans_manager.AI.modal.LoanAiAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanAiAssessmentRepository extends JpaRepository<LoanAiAssessment, Long> {
    Optional<LoanAiAssessment> findByLoanId(Long loanId);
    boolean existsByLoanId(Long loanId);
}
