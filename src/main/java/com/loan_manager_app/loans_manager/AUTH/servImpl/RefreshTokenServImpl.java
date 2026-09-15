package com.loan_manager_app.loans_manager.AUTH.servImpl;


import com.loan_manager_app.loans_manager.AUTH.events.RefreshTokenSession;
import com.loan_manager_app.loans_manager.AUTH.modals.RefreshToken;
import com.loan_manager_app.loans_manager.AUTH.modals.Users;
import com.loan_manager_app.loans_manager.AUTH.repo.RefreshTokenRepository;
import com.loan_manager_app.loans_manager.AUTH.service.RefreshTokenService;
import com.loan_manager_app.loans_manager.AUTH.service.TokenGeneratorService;
import com.loan_manager_app.loans_manager.AUTH.service.TokenHashService;
import com.loan_manager_app.loans_manager.SHARED.ENUMS.RefreshTokenStatus;
import com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions.InvalidRefreshTokenException;
import com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions.RefreshTokenExpiredException;
import com.loan_manager_app.loans_manager.SHARED.Exceptions.authExceptions.tokensExceptions.RefreshTokenReuseDetectedException;
import com.loan_manager_app.loans_manager.SHARED.GlobalEvents.ClientInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenGeneratorService tokenGeneratorService;
    private final TokenHashService tokenHashService;
    private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(30);

    @Override
    public RefreshTokenSession createSession(
            Users user,
            String device,
            String ipAddress
    ) {
        return issueSession(
                user,
                UUID.randomUUID(),
                device,
                ipAddress
        );
    }

    @Transactional
    @Override
    public RefreshToken verify(String rawRefreshToken) {
        RefreshToken refreshToken =
                getRefreshToken(rawRefreshToken);
        refreshToken.validate();
        refreshToken.markAsUsed();
        return refreshToken;
    }

    @Override
    @Transactional
    public RefreshTokenSession rotate(String rawRefreshToken, ClientInfo clientInfo) {
        RefreshToken refreshToken = verify(rawRefreshToken);

        markAsRotated(refreshToken);

        return issueSession(
                refreshToken.getUser(),
                refreshToken.getSessionId(),
                refreshToken.getDevice(),
                refreshToken.getIpAddress()
        );
    }

    @Override
    @Transactional
    public void revoke(String rawToken) {
        findRefreshToken(rawToken)
                .ifPresent(RefreshToken::revoke);
    }

    private RefreshToken getRefreshToken(String rawToken) {
        return findRefreshToken(rawToken)
                .orElseThrow(() ->
                        new InvalidRefreshTokenException(
                                "Refresh token not found."
                        ));
    }

    @Override
    public int revokeAll(Users user) {
        int revokedCount =
                refreshTokenRepository.updateStatusForAllUserSessions(user, RefreshTokenStatus.REVOKED);
        return revokedCount;
    }

    @Override
    public void deleteExpiredSessions() {

    }

    @Override
    public void handleInvalidStatus(RefreshToken refreshToken) {
        switch (refreshToken.getStatus()){
            case ACTIVE -> {
                return;
            }

            case EXPIRED -> throw new RefreshTokenExpiredException("Refresh token has expired");
            case REVOKED -> throw new InvalidRefreshTokenException("Refresh token has been revoked");
            case ROTATED -> {
                revokeSession(refreshToken.getSessionId());
                throw new RefreshTokenReuseDetectedException("Refresh token reuse detected");
            }
        }
    }

    private void revokeSession(
            UUID sessionId
    ) {

        refreshTokenRepository.updateStatusBySessionId(
                sessionId,
                RefreshTokenStatus.REVOKED
        );

    }

    private RefreshTokenSession issueSession(
            Users user,
            UUID sessionId,
            String device,
            String ipAddress
    ) {

        String rawToken = tokenGeneratorService.generateRefreshToken();
        String tokenHash = tokenHashService.hashToken(rawToken);

        Instant now = Instant.now();
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .sessionId(sessionId)
                .tokenHash(tokenHash)
                .status(RefreshTokenStatus.ACTIVE)
                .device(device)
                .ipAddress(ipAddress)
                .lastUsedAt(now)
                .expiresAt(now.plus(REFRESH_TOKEN_TTL))
                .build();

        RefreshToken saved =
                refreshTokenRepository.save(refreshToken);

        return new RefreshTokenSession(
                rawToken,
                saved
        );
    }

    private void markAsRotated(
            RefreshToken refreshToken
    ) {

        int updated = refreshTokenRepository.rotateIfActive(
                refreshToken.getId()
        );

        if (updated == 0) {
            throw new RefreshTokenReuseDetectedException(
                    "Refresh token was already rotated."
            );
        }
    }

    private Optional<RefreshToken> findRefreshToken(String rawToken) {
        String tokenHash = tokenHashService.hashToken(rawToken);
        return refreshTokenRepository.findByTokenHash(tokenHash);
    }
}
