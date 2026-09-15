package com.loan_manager_app.loans_manager.AUTH.repo;


import com.loan_manager_app.loans_manager.AUTH.modals.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@NamedInterface
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
    Page<Users> findAllBy(Pageable pageable);
}
