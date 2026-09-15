package com.loan_manager_app.loans_manager.AUDIT.repo;


import com.loan_manager_app.loans_manager.AUDIT.modal.Audit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
    @Override
    Page<Audit> findAll(Pageable pageable);
}
