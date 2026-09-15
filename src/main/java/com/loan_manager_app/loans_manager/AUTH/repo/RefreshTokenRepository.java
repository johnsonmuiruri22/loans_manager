package com.loan_manager_app.loans_manager.AUTH.repo;


import com.loan_manager_app.loans_manager.AUTH.modals.RefreshToken;
import com.loan_manager_app.loans_manager.AUTH.modals.Users;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.RefreshTokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    Optional<RefreshToken>
    findByTokenHashAndStatus(
            String tokenHash,
            RefreshTokenStatus status
    );

    List<RefreshToken>
    findByUserAndStatus(
            Users user,
            RefreshTokenStatus status
    );

    long countByUserAndStatus(
            Users user,
            RefreshTokenStatus status
    );

    @Modifying
    @Query("""
UPDATE RefreshToken rt
SET rt.status = :status
WHERE rt.user = :user
AND rt.status = 'ACTIVE'
""")
    int updateStatusForAllUserSessions(
            Users user,
            RefreshTokenStatus status
    );

    void deleteByExpiresAtBefore(Instant now);
    void deleteByStatus(RefreshTokenStatus status);

    @Modifying
    @Query("""
DELETE FROM RefreshToken rt
WHERE rt.status = 'REVOKED'
""")
    void deleteRevokedTokens();

    List<RefreshToken> findBySessionId(UUID sessionId);

    @Modifying
    @Query("""
UPDATE RefreshToken rt
SET rt.status = :status
WHERE rt.sessionId = :sessionId
""")
    int updateStatusBySessionId(
            UUID sessionId,
            RefreshTokenStatus status
    );

    @Modifying
    @Query("""
UPDATE RefreshToken rt
SET rt.status = 'ROTATED'
WHERE rt.id = :id
AND rt.status = 'ACTIVE'
""")
    int rotateIfActive(Long id);
}
