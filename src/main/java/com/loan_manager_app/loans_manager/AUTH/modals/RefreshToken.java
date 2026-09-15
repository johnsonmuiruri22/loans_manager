package com.loan_manager_app.loans_manager.AUTH.modals;


import com.loan_manager_app.loans_manager.SHARED.ENUMS.RefreshTokenStatus;
import com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions.InvalidRefreshTokenException;
import com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions.RefreshTokenExpiredException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Getter @Setter
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @Column(nullable = false, unique = true)
    private String tokenHash;

    @Column(nullable = false)
    private Instant expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefreshTokenStatus status;

    private String device;
    private String ipAddress;

    private Instant lastUsedAt;

    @CreationTimestamp
    @Column(
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    private Instant lastIssuedAt;

    @Column(nullable = false)
    private UUID sessionId;

    private Instant revokedAt;

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public void revoke() {
        if (this.status == RefreshTokenStatus.REVOKED) {
            return;
        }
        this.status = RefreshTokenStatus.REVOKED;
        this.revokedAt = Instant.now();
    }

    public void rotate() {
        this.status = RefreshTokenStatus.ROTATED;
        this.lastIssuedAt = Instant.now();
    }

    public void markAsUsed() {
        this.lastUsedAt = Instant.now();
    }

    public void expire() {
        this.status = RefreshTokenStatus.EXPIRED;
    }

    public boolean isActive() {
        return this.status == RefreshTokenStatus.ACTIVE;
    }

    public boolean isRevoked() {
        return this.status == RefreshTokenStatus.REVOKED;
    }

    public boolean isRotated() {
        return this.status == RefreshTokenStatus.ROTATED;
    }

    public boolean canBeRefreshed() {
        return status == RefreshTokenStatus.ACTIVE
                && !isExpired();
    }

    public void validate() {
        if (status == RefreshTokenStatus.REVOKED) {
            throw new InvalidRefreshTokenException(
                    "Refresh token has been revoked."
            );
        }

        if (status == RefreshTokenStatus.ROTATED) {
            throw new InvalidRefreshTokenException(
                    "Refresh token has already been rotated."
            );
        }

        if (status == RefreshTokenStatus.EXPIRED) {
            throw new RefreshTokenExpiredException(
                    "Refresh token has expired."
            );
        }
        if (isExpired()) {
            expire();
            throw new RefreshTokenExpiredException(
                    "Refresh token has expired."
            );
        }
    }
}
